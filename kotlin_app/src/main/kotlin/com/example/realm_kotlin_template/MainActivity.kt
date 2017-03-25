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
        val text1: TextView
        val text2: TextView

        init {

            text1 = itemView.findViewById(android.R.id.text1) as TextView
            text2 = itemView.findViewById(android.R.id.text2) as TextView
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (getApplication() as MyApplication).component?.inject(this)

        val list = findViewById(R.id.list) as RecyclerView

        list.setHasFixedSize(true)
        list.setLayoutManager(LinearLayoutManager(this))
        list.setAdapter(object : RealmRecyclerViewAdapter<User, ViewHolder>(realm.where(User::class.java).findAll(), true) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2,
                        parent,
                        false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val user = getItem(position)

                holder.text1.setText(user?.name)
                holder.text2.text = String.format(Locale.getDefault(), "%1\$d", user?.age)
            }
        })

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.argb(30, 0, 0, 0)
        val dividerHeight = (2 * getResources().getDisplayMetrics().density).toInt()

        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = dividerHeight
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)

                val manager = parent.getLayoutManager()
                val left = parent.getPaddingLeft()
                val right = parent.getWidth() - parent.getPaddingRight()
                val childCount = parent.getChildCount()
                for (i in 0..childCount - 1) {
                    val child = parent.getChildAt(i)
                    val params = child.getLayoutParams() as RecyclerView.LayoutParams

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
        getMenuInflater().inflate(R.menu.menu_main, menu)
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
        val allUsers : RealmResults<User> = realm.where(User::class.java).findAll()
        realm.executeTransaction {
            // don't use for-each. see https://github.com/realm/realm-java/issues/640
            for (i in 0..allUsers.size - 1) {
                val user = allUsers.get(i)
                user.age = user.age + 1
            }
        }
    }

}
