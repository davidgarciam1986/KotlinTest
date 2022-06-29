package com.example.kotlintest.repository

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintest.R
import com.example.kotlintest.common.model.RepositoryDetails
import com.example.kotlintest.common.model.RepositoryTree
import com.example.kotlintest.common.model.TreeItem
import com.example.kotlintest.databinding.ActivityDashboardBinding
import com.example.kotlintest.databinding.ActivityRepositoryViewBinding

class RepositoryActivity : AppCompatActivity(), RepositoryView {
    lateinit var presenter: RepositoryPresenter
    lateinit var progressBarOverlay: FrameLayout
    lateinit var fullName: String
    lateinit var tree: String

    private lateinit var activityRepositoryBinding: ActivityRepositoryViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRepositoryBinding = ActivityRepositoryViewBinding.inflate(layoutInflater)
        setContentView(activityRepositoryBinding.root)

        val imageButton: ImageButton = activityRepositoryBinding.imageButton
        imageButton.setOnClickListener { onBackPressed() }
        presenter = RepositoryPresenterImpl(this)
        fullName = getIntent().getStringExtra("name").toString()
        tree = getIntent().getStringExtra("tree").toString()

        //Mostrar barra de progreso y cargar datos
        progressBarOverlay = activityRepositoryBinding.progressBarOverlay1
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
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show()
    }

    override fun showRepository(repository: RepositoryDetails) {
        //rellenar datos en la vista
        val fullName: TextView = activityRepositoryBinding.textView3
        val description: TextView = activityRepositoryBinding.textView4
        val language: TextView = activityRepositoryBinding.textView5
        fullName.setText(repository.getFullName())
        description.setText(repository.getDescription())
        language.text = "Language: " + repository.getLanguage()
    }

    override fun showTree(tree: RepositoryTree) {
        try {
            val treeItems: List<TreeItem> = tree.getTree()
            val files: LinearLayout = activityRepositoryBinding.scrollView1
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
