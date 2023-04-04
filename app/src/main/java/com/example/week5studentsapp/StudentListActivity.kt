package com.example.week5studentsapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week5studentsapp.HelperClasses.Adapters.StudentRecyclerViewAdapter
import com.example.week5studentsapp.HelperClasses.DataClasses.Student
import com.example.week5studentsapp.HelperClasses.Database.TableStudent
import com.example.week5studentsapp.HelperClasses.General.PublicConstants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class StudentListActivity : AppCompatActivity(), StudentRecyclerViewAdapter.OnItemClickListener,
    StudentRecyclerViewAdapter.OnItemLongClickListener {

    // Declare Views.
    private lateinit var recyclerViewStudents: RecyclerView
    private lateinit var fabCreateNewStudent: FloatingActionButton
    private lateinit var editTextSearchInput: EditText

    // Recycler View adapter
    private lateinit var studentRecyclerAdapter: StudentRecyclerViewAdapter

    // Variables.
    private lateinit var studentsListForRecycler: ArrayList<Student>
    private lateinit var studentsListForRecyclerFilter: ArrayList<Student>
    private var buttonClickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)
        initializeComponents()
    }

    override fun onStart() {
        super.onStart()

        // Get the recently added students to arraylist and update it into the adapter class.
        studentsListForRecycler = getAllStudents()
        studentRecyclerAdapter.updateStudentList(studentsListForRecycler)
    }

    private fun initializeComponents() {
        // View hooks.
        editTextSearchInput = findViewById(R.id.et_student_search)

        // Text watcher for student search edittext.
        editTextSearchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterRecyclerView()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        // Floating action button hook and listener.
        fabCreateNewStudent = findViewById(R.id.fab_student_list)
        fabCreateNewStudent.setOnClickListener(onClickListenerCreateNewStudent)

        // Array list student for filtering students.
        studentsListForRecyclerFilter = arrayListOf()

        // Get all student data from database and store it into array list.
        studentsListForRecycler = arrayListOf()
        studentsListForRecycler = getAllStudents()

        // Recycler view.
        recyclerViewStudents = findViewById(R.id.rv_students)
        recyclerViewStudents.layoutManager = GridLayoutManager(this, 3)
        recyclerViewStudents.setHasFixedSize(true)

        // Initialize student recycler view adapter object and set it for recycler view.
        studentRecyclerAdapter =
            StudentRecyclerViewAdapter(studentsListForRecycler, this, this)
        recyclerViewStudents.adapter = studentRecyclerAdapter
    }

    private fun getAllStudents(): ArrayList<Student> {
        val tableStudent = TableStudent()
        val cursor = tableStudent.viewAllStudents()
        val students: ArrayList<Student> = arrayListOf()

        // Move the cursor to first position if not null.
        cursor!!.moveToFirst()

        // Iterate through cursor.
        var studentImage: ByteArray?
        while (cursor.moveToNext()) {
            // Get image blob as byte array from cursor.
            studentImage = cursor.getBlob(cursor.getColumnIndexOrThrow(TableStudent.COL_IMAGE))
            val student = Student(
                cursor.getInt(cursor.getColumnIndexOrThrow(TableStudent.COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableStudent.COL_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableStudent.COL_LAST_NAME)),
                null,
                null,
                studentImage
            )

            // Add student object to students list.
            students.add(student)
        }
        return students
    }

    private fun deleteStudent(view: View, position: Int) {
        val studentDatabase = TableStudent()
        if (studentDatabase.deleteStudent(view.tag.toString().toInt())) {
            studentsListForRecycler.removeAt(position)
            studentRecyclerAdapter.notifyItemRemoved(position)
            Toast.makeText(this, getString(R.string.student_deleted), Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterRecyclerView() {
        // Clear filter arraylist avoiding duplicate entries.
        studentsListForRecyclerFilter.clear()

        // Get the search element from edittext.
        val searchText = editTextSearchInput.text.toString().lowercase(Locale.getDefault()).trim()

        // Add the students which search element in their first or last name.
        if (searchText.isNotEmpty()) {
            studentsListForRecycler.forEach {
                if (it.firstName.lowercase(Locale.getDefault())
                        .contains(searchText.lowercase(Locale.getDefault())) ||
                    it.lastName.lowercase(Locale.getDefault())
                        .contains(searchText.lowercase(Locale.getDefault())) ||
                    "${it.firstName} ${it.lastName}".lowercase(Locale.getDefault())
                        .contains(searchText.lowercase(Locale.getDefault()))
                ) {
                    studentsListForRecyclerFilter.add(it)
                }
            }
            studentRecyclerAdapter.updateStudentList(studentsListForRecyclerFilter)
        } else {
            // If search element is empty reset the filter set with whole students list.
            studentsListForRecyclerFilter.clear()
            studentsListForRecyclerFilter.addAll(studentsListForRecycler)
            studentRecyclerAdapter.updateStudentList(studentsListForRecycler)
        }
    }

    // Listeners
    // Recycler view item click listener.
    override fun onItemClick(v: View, position: Int) {
        // Increase count by times the button pressed.
        buttonClickCount++

        // Handle the code after 250 ms checking whether the button clicked once or twice.
        Handler(Looper.getMainLooper()).postDelayed({
            if (buttonClickCount == 1) {
                // Button clicked once.
                // Open student view activity.
                val intent = Intent(this, StudentDetailsActivity::class.java)

                // Pass student id as extra.
                intent.putExtra(PublicConstants.STUDENT_ID, v.tag.toString())
                startActivity(intent)

            } else if (buttonClickCount == 2) {
                // Button clicked twice.
                // Open create student activity in edit mode.
                val intent = Intent(this, StudentCreateActivity::class.java)

                // Pass extra, form submit mode as edit.
                intent.putExtra(
                    PublicConstants.FORM_SUBMIT_MODE, PublicConstants.FORM_SUBMIT_MODE_EDIT
                )
                intent.putExtra(PublicConstants.STUDENT_ID, v.tag.toString())
                startActivity(intent)
            }
            buttonClickCount = 0
        }, 250)
    }

    // Recycler view item long click listener.
    override fun onItemLongClick(v: View, position: Int) {
        // Create alert box for confirm delete student entry.
        AlertDialog.Builder(this).apply {
            this.setTitle(getString(R.string.delete_student))
            this.setMessage(getString(R.string.are_you_sure_delete))
            this.setIcon(android.R.drawable.ic_delete)
            this.setPositiveButton(android.R.string.yes) { _, _ ->
                // Confirmed to delete student entry.
                deleteStudent(view = v, position = position)
            }
            this.setNegativeButton(android.R.string.no) { _, _ ->
                // Cancelled to confirmation and do nothing.
            }
        }.show()
    }

    //FAB click listener for creating new student.
    private val onClickListenerCreateNewStudent: OnClickListener = OnClickListener {
        val intent = Intent(this, StudentCreateActivity::class.java)

        // Pass extra, form submit mode as add.
        intent.putExtra(PublicConstants.FORM_SUBMIT_MODE, PublicConstants.FORM_SUBMIT_MODE_ADD)
        startActivity(intent)
    }
}