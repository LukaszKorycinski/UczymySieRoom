package com.example.uczymysieroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.Completable



class MainActivity : AppCompatActivity() {

    private var chapterDatabase: ChapterDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chapterDatabase = ChapterDatabase.getDatabase(this)

        addBtn.setOnClickListener { addOnClick() }
        refreshBtn.setOnClickListener { refreshOnClick() }
    }

    fun addOnClick() {
        val chapter  = Chapter(addTV.text.toString())
        Completable.fromAction { chapterDatabase!!.chapterDao().insert(chapter) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(this, "dodano", Toast.LENGTH_SHORT).show()
                addTV.text.clear()
            })
        }

    fun refreshOnClick() {
        chapterDatabase!!.chapterDao().getAllChapter()
            .observeOn(AndroidSchedulers.mainThread())//result
            .subscribeOn(Schedulers.io())//all
            .subscribe(  {
                    chapterList ->
                    var full=""
                    for (i in 0..chapterList.size - 1) {
                        full = full + chapterList.get(i).chapterName
                    }
                    resultsTV.text=full
            }  )
    }
}
