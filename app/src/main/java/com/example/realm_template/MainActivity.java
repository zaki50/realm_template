package com.example.realm_template;

import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realm_template.databinding.ActivityMainBinding;
import com.example.realm_template.model.User;

import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Inject
    Realm realm;

    private static final class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView text1;
        public final TextView text2;

        public ViewHolder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(android.R.id.text1);
            text2 = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getComponent().inject(this);

        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final RecyclerView list = binding.list;

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new RealmRecyclerViewAdapter<User, ViewHolder>(this, realm.where(User.class).findAll(), true) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2,
                        parent,
                        false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                final User user = getItem(position);

                holder.text1.setText(user.name);
                holder.text2.setText(String.format(Locale.getDefault(), "%1$d", user.age));
            }
        });

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.argb(30, 0, 0, 0));
        final int dividerHeight = (int) (2 * getResources().getDisplayMetrics().density);

        list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = dividerHeight;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);

                final RecyclerView.LayoutManager manager = parent.getLayoutManager();
                final int left = parent.getPaddingLeft();
                final int right = parent.getWidth() - parent.getPaddingRight();
                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params =
                            (RecyclerView.LayoutParams) child.getLayoutParams();

                    // ViewCompat.getTranslationY()を入れないと
                    // 追加・削除のアニメーション時の位置が変になる
                    final int top = manager.getDecoratedBottom(child)
                            - params.topMargin
                            + Math.round(ViewCompat.getTranslationY(child));
                    final int bottom = top + dividerHeight;
                    c.drawRect(left, top, right, bottom, paint);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
        realm = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_data:
                importData();
                return true;
            case R.id.action_increment_all_ages:
                incrementAge();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void importData() {
        final Random random = new Random();

        final User user1 = new User();
        user1.name = "Ichiro Suzuki";
        user1.age = 20 + random.nextInt(20);
        final User user2 = new User();
        user2.name = "Jiro Yamada";
        user2.age = 20 + random.nextInt(20);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(User.class).findAll().deleteAllFromRealm();

                realm.copyToRealm(user1);
                realm.copyToRealm(user2);
            }
        });
    }

    private void incrementAge() {
        final RealmResults<User> allUsers = realm.where(User.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // don't use for-each. see https://github.com/realm/realm-java/issues/640
                for (int i = 0; i < allUsers.size(); i++) {
                    final User user = allUsers.get(i);
                    user.age = user.age + 1;
                }
            }
        });
    }

}
