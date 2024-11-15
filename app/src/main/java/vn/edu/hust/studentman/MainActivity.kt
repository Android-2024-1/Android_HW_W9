package vn.edu.hust.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  private lateinit var studentAdapter: StudentAdapter
  private lateinit var recyclerView: RecyclerView

  private val students = mutableListOf<StudentModel>().apply {
    addAll(listOf(
      StudentModel("Nguyễn Văn An", "SV001"),
      StudentModel("Trần Thị Bảo", "SV002"),
      StudentModel("Lê Hoàng Cường", "SV003"),
      StudentModel("Phạm Thị Dung", "SV004"),
      StudentModel("Đỗ Minh Đức", "SV005"),
      StudentModel("Vũ Thị Hoa", "SV006"),
      StudentModel("Hoàng Văn Hải", "SV007"),
      StudentModel("Bùi Thị Hạnh", "SV008"),
      StudentModel("Đinh Văn Hùng", "SV009"),
      StudentModel("Nguyễn Thị Linh", "SV010"),
      StudentModel("Phạm Văn Long", "SV011"),
      StudentModel("Trần Thị Mai", "SV012"),
      StudentModel("Lê Thị Ngọc", "SV013"),
      StudentModel("Vũ Văn Nam", "SV014"),
      StudentModel("Hoàng Thị Phương", "SV015"),
      StudentModel("Đỗ Văn Quân", "SV016"),
      StudentModel("Nguyễn Thị Thu", "SV017"),
      StudentModel("Trần Văn Tài", "SV018"),
      StudentModel("Phạm Thị Tuyết", "SV019"),
      StudentModel("Lê Văn Vũ", "SV020")
    ))
  }

  private var lastDeletedStudent: StudentModel? = null
  private var lastDeletedPosition: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupViews()
  }

  private fun setupViews() {
    // Setup RecyclerView
    recyclerView = findViewById(R.id.recycler_view_students)
    studentAdapter = StudentAdapter(students, this)
    recyclerView.apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
      setHasFixedSize(true)
    }

    // Setup Add button
    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog()
    }
  }

  private fun showAddStudentDialog() {
    val dialogView = LayoutInflater.from(this)
      .inflate(R.layout.dialog_add_edit_student, null)

    val nameEdit = dialogView.findViewById<EditText>(R.id.edit_student_name)
    val idEdit = dialogView.findViewById<EditText>(R.id.edit_student_id)

    AlertDialog.Builder(this)
      .setTitle("Add New Student")
      .setView(dialogView)
      .setPositiveButton("Add") { _, _ ->
        val name = nameEdit.text.toString().trim()
        val id = idEdit.text.toString().trim()
        if (name.isNotEmpty() && id.isNotEmpty()) {
          val newStudent = StudentModel(name, id)
          students.add(newStudent)
          studentAdapter.notifyItemInserted(students.size - 1)
        }
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  fun onEditClick(position: Int) {
    val student = students[position]
    val dialogView = LayoutInflater.from(this)
      .inflate(R.layout.dialog_add_edit_student, null)

    val nameEdit = dialogView.findViewById<EditText>(R.id.edit_student_name)
    val idEdit = dialogView.findViewById<EditText>(R.id.edit_student_id)

    nameEdit.setText(student.studentName)
    idEdit.setText(student.studentId)

    AlertDialog.Builder(this)
      .setTitle("Edit Student")
      .setView(dialogView)
      .setPositiveButton("Update") { _, _ ->
        val name = nameEdit.text.toString().trim()
        val id = idEdit.text.toString().trim()
        if (name.isNotEmpty() && id.isNotEmpty()) {
          students[position] = StudentModel(name, id)
          studentAdapter.notifyItemChanged(position)
        }
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  fun onDeleteClick(position: Int) {
    AlertDialog.Builder(this)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete this student?")
      .setPositiveButton("Yes") { _, _ ->
        deleteStudent(position)
      }
      .setNegativeButton("No", null)
      .show()
  }

  private fun deleteStudent(position: Int) {
    lastDeletedStudent = students[position]
    lastDeletedPosition = position
    students.removeAt(position)
    studentAdapter.notifyItemRemoved(position)

    Snackbar.make(
      recyclerView,
      "Student deleted",
      Snackbar.LENGTH_LONG
    ).setAction("Undo") {
      lastDeletedStudent?.let { student ->
        students.add(lastDeletedPosition, student)
        studentAdapter.notifyItemInserted(lastDeletedPosition)
      }
    }.show()
  }
}