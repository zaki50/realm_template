package com.example.realm_template;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realm_template.databinding.ActivityMainBinding;
import com.example.realm_template.model.User;

import java.util.Random;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Inject
    Realm realm;

    private static final class ViewHolder {
        public final TextView text1;
        public final TextView text2;

        public ViewHolder(TextView text1, TextView text2) {
            this.text1 = text1;
            this.text2 = text2;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getComponent().inject(this);

        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.list.setAdapter(new RealmBaseAdapter<User>(this, realm.allObjects(User.class), true) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
                    holder = new ViewHolder((TextView) convertView.findViewById(android.R.id.text1),
                            (TextView) convertView.findViewById(android.R.id.text2));
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                final User item = getItem(position);

                holder.text1.setText(item.getName());
                holder.text2.setText(Integer.toString(item.getAge()));

                return convertView;
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
        user1.setName("Ichiro Suzuki");
        user1.setAge(20 + random.nextInt(20));
        final User user2 = new User();
        user2.setName("Jiro Yamada");
        user2.setAge(20 + random.nextInt(20));

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.allObjects(User.class).clear();

                realm.copyToRealm(user1);
                realm.copyToRealm(user2);
            }
        });
    }

    private void incrementAge() {
        final RealmResults<User> allUsers = realm.allObjects(User.class);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // don't use for-each. see https://github.com/realm/realm-java/issues/640
                for (int i = 0; i < allUsers.size(); i++) {
                    final User user = allUsers.get(i);
                    user.setAge(user.getAge() + 1);
                }
            }
        });
    }

}
