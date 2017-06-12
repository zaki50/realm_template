package com.example.realm_kotlin_template

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.realm_kotlin_template.model.User

import java.util.Locale
import java.util.Random

import javax.inject.Inject

import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var realm: Realm

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text1: TextView = itemView.findViewById(android.R.id.text1)
        val text2: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as MyApplication).component?.inject(this)

        val list: RecyclerView = findViewById(R.id.list)

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = object : RealmRecyclerViewAdapter<User, ViewHolder>(realm.where(User::class.java).findAll(), true) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = layoutInflater.inflate(android.R.layout.simple_list_item_2,
                        parent,
                        false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val user = getItem(position)

                holder.text1.text = user?.name
                holder.text2.text = String.format(Locale.getDefault(), "%1\$d", user?.age)
            }
        }

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.argb(30, 0, 0, 0)
        val dividerHeight = (2 * resources.displayMetrics.density).toInt()

        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = dividerHeight
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)

                val manager = parent.layoutManager
                val left = parent.paddingLeft
                val right = parent.width - parent.paddingRight
                val childCount = parent.childCount
                for (i in 0..childCount - 1) {
                    val child = parent.getChildAt(i)
                    val params = child.layoutParams as RecyclerView.LayoutParams

                    // ViewCompat.getTranslationY()を入れないと
                    // 追加・削除のアニメーション時の位置が変になる
                    val top = manager.getDecoratedBottom(child) - params.topMargin + Math.round(ViewCompat.getTranslationY(child))
                    val bottom = top + dividerHeight
                    c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        realm.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_import_data -> {
                importData()
                return true
            }
            R.id.action_increment_all_ages -> {
                incrementAge()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun importData() {
        val random = Random()

        val user1 = User()
        user1.name = "Ichiro Suzuki"
        user1.age = 20 + random.nextInt(20)
        val user2 = User()
        user2.name = "Jiro Yamada"
        user2.age = 20 + random.nextInt(20)

        realm.executeTransaction { realm ->
            realm.where(User::class.java).findAll().deleteAllFromRealm()

            realm.copyToRealm(user1)
            realm.copyToRealm(user2)
        }
    }

    private fun incrementAge() {
        realm.executeTransaction {
            val allUsers : RealmResults<User> = realm.where(User::class.java).findAll()
            allUsers.forEach { it.age++ }
        }
    }

}
