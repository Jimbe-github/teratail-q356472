package com.teratail.q356472;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  class Question {
    final String title;
    final int id;
    final int number;
    final String text;
    final int[] drawableIds;
    Question(String title, int id, int number, String text, int[] drawableIds) {
      this.title = title;
      this.id = id;
      this.number = number;
      this.text = text;
      this.drawableIds = drawableIds;
    }
  }

  private Question questions[] = new Question[]{
          new Question("問題1", Menu.FIRST+0, 1, "画像無しな問題", null),
          new Question("問題2", Menu.FIRST+1, 2, "画像付き(1枚)", new int[]{R.drawable.question2_1}),
          new Question("問題3", Menu.FIRST+2, 3, "画像が二枚あれば\n切り替え可能", new int[]{R.drawable.question1_1, R.drawable.question1_2}),
  };

  private TextView numberTextView;
  private int selected_question_index;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    numberTextView = findViewById(R.id.question_target);

    setQuestion(0);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    SubMenu subMenu = menu.addSubMenu("問題選択");
    for(Question q : questions) {
      subMenu.add(Menu.FIRST, q.id, Menu.NONE, q.title);
    }
    subMenu.setGroupCheckable(Menu.FIRST, true, true);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    menu.findItem(questions[selected_question_index].id).setChecked(true);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    for(int i=0; i<questions.length; i++) {
      if(item.getItemId() == questions[i].id) {
        setQuestion(i);
        return true;
      }
    }
    return false;
  }

  //問題をセット
  private void setQuestion(int index) {
    selected_question_index = index;
    numberTextView.setText(""+questions[index].number);

    Fragment fragment = MainFragment.newInstance(questions[index].text, questions[index].drawableIds);
    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
  }
}