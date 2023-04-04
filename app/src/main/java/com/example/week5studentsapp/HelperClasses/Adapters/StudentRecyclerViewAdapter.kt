package com.example.week5studentsapp.HelperClasses.Adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.week5studentsapp.HelperClasses.DataClasses.Student
import com.example.week5studentsapp.R

class StudentRecyclerViewAdapter(
    private var studentList: ArrayList<Student>,
    private val clickListener: OnItemClickListener,
    private val longClickListener: OnItemLongClickListener
) : RecyclerView.Adapter<StudentRecyclerViewAdapter.MyViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_student_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Set data for views.
        val currentItem = studentList[position]

        //Converting byte array to bitmap and set to image view.
        if (currentItem.image != null)
            holder.itemImage.setImageBitmap(
                BitmapFactory.decodeByteArray(currentItem.image, 0, currentItem.image.size)
            )
        else {
            holder.itemImage.setImageResource(R.drawable.default_pic)
        }

        // Append first and last name.
        holder.itemName.text = buildString {
            append(currentItem.firstName)
            append(" ")
            append(currentItem.lastName)
        }

        // Set student id to student card item as tag.
        holder.cardItem.tag = currentItem.id
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun updateStudentList(studentList: ArrayList<Student>) {
        this.studentList = studentList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        val itemImage: ImageView = itemView.findViewById(R.id.iv_card_student_image)
        val itemName: TextView = itemView.findViewById(R.id.tv_card_student_name)
        val cardItem: CardView = itemView.findViewById(R.id.card_student_item)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            if (adapterPosition != RecyclerView.NO_POSITION)
                clickListener.onItemClick(v, adapterPosition)
        }

        override fun onLongClick(v: View): Boolean {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                longClickListener.onItemLongClick(v, adapterPosition)
                return true
            }
            return false
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(v: View, position: Int)
    }
}
