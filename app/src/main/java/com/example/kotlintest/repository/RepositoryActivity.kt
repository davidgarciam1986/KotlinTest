package com.example.kotlintest.repository

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintest.R
import com.example.kotlintest.common.model.RepositoryDetails
import com.example.kotlintest.common.model.RepositoryTree
import com.example.kotlintest.common.model.TreeItem

class RepositoryActivity : AppCompatActivity(), RepositoryView {
    lateinit var presenter: RepositoryPresenter
    lateinit var progressBarOverlay: FrameLayout
    lateinit var fullName: String
    lateinit var tree: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_view)

        val imageButton: ImageButton = findViewById(R.id.imageButton)
        imageButton.setOnClickListener { onBackPressed() }
        presenter = RepositoryPresenterImpl(this)
        fullName = getIntent().getStringExtra("name").toString()
        tree = getIntent().getStringExtra("tree").toString()

        //Mostrar barra de progreso y cargar datos
        progressBarOverlay = findViewById(R.id.progressBarOverlay1)
        progressBarOverlay.visibility = View.GONE
        presenter.getRepository(fullName)
        presenter.getTree(fullName, tree)
    }

    override fun showProgressBar() {
        progressBarOverlay.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBarOverlay.visibility = View.GONE
    }

    override fun showError(error: String) {
        //TODO
    }

    override fun showRepository(repository: RepositoryDetails) {
        //rellenar datos en la vista
        val fullName: TextView = findViewById(R.id.textView3)
        val description: TextView = findViewById(R.id.textView4)
        val language: TextView = findViewById(R.id.textView5)
        fullName.setText(repository.getFullName())
        description.setText(repository.getDescription())
        language.text = "Language: " + repository.getLanguage()
    }

    override fun showTree(tree: RepositoryTree) {
        try {
            val treeItems: List<TreeItem> = tree.getTree()
            val files: LinearLayout = findViewById(R.id.scrollView1)
            val lparams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)


            //Carpetas aparecen primero en la lista
            for (i in 0 until treeItems.size - 1) {
                if (treeItems[i].getType().equals("tree")) {
                    val textView = TextView(this)
                    textView.layoutParams = lparams
                    textView.setTextColor(Color.WHITE)
                    textView.setTypeface(null, Typeface.BOLD)
                    textView.setText(treeItems[i].getPath())
                    files.addView(textView)
                    val sha: String = treeItems[i].getSha()
                    textView.setOnClickListener {
                        val intent = Intent(this,
                            RepositoryActivity::class.java)
                        intent.putExtra("name", fullName)
                        intent.putExtra("tree", sha)
                        startActivity(intent)
                    }
                }
            }

            //Archivos aparecen despues
            for (i in 0 until treeItems.size - 1) {
                if (treeItems[i].getType().equals("blob")) {
                    val textView = TextView(this)
                    textView.layoutParams = lparams
                    textView.setTextColor(Color.WHITE)
                    textView.setTypeface(null, Typeface.ITALIC)
                    textView.setText(treeItems[i].getPath())
                    files.addView(textView)
                }
            }
        } catch (e: Exception) {
            showError("Repositorio vacio.")
        }
    }
}
