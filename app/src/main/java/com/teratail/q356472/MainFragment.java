package com.teratail.q356472;

import android.os.Bundle;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;

import android.view.*;
import android.view.animation.*;
import android.widget.*;

public class MainFragment extends Fragment {
  private static final String TAG = "MainFragment";
  protected static final String PARAM_TEXT = "text";
  protected static final String PARAM_DRAWABLES = "drawables";

  public static MainFragment newInstance(String text, int[] drawables) {
    if(text == null) throw new NullPointerException();
    MainFragment fragment = new MainFragment();
    Bundle args = new Bundle();
    args.putString(PARAM_TEXT, text);
    if(drawables != null && drawables.length >= 1) args.putIntArray(PARAM_DRAWABLES, drawables);
    fragment.setArguments(args);
    return fragment;
  }

  private String text;
  private int[] drawables; //画像リソースID (null なら画像無し)

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if(getArguments() != null) {
      text = getArguments().getString(PARAM_TEXT);
      drawables = getArguments().getIntArray(PARAM_DRAWABLES);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    TextView question = view.findViewById(R.id.question);
    question.setText(text);

    ViewGroup frame = view.findViewById(R.id.frame);
    if(drawables == null || drawables.length == 0) {
      frame.setVisibility(View.GONE);
      return;
    }
    frame.setVisibility(View.VISIBLE);

    ViewFlipper flipper = view.findViewById(R.id.flipper);
    for(int id : drawables) {
      SGImageView imageView = new SGImageView(getContext());
      imageView.setImageResource(id);
      flipper.addView(imageView);
    }

    ViewGroup controller = view.findViewById(R.id.flipper_controller);
    if(drawables.length <= 1) {
      controller.setVisibility(View.GONE);
      return;
    }
    controller.setVisibility(View.VISIBLE);

    Button prevButton = view.findViewById(R.id.button_prev);
    prevButton.setOnClickListener(v -> {
      flipper.setInAnimation(inFromLeftAnimation);
      flipper.setOutAnimation(outToRightAnimation);
      flipper.showPrevious();
    });

    Button nextButton = view.findViewById(R.id.button_next);
    nextButton.setOnClickListener(v -> {
      flipper.setInAnimation(inFromRightAnimation);
      flipper.setOutAnimation(outToLeftAnimation);
      flipper.showNext();
    });
  }

  private static final Animation inFromRightAnimation = getAnimation(1.0f, 0.0f);
  private static final Animation inFromLeftAnimation = getAnimation(-1.0f, 0.0f);
  private static final Animation outToLeftAnimation = getAnimation(0.0f, -1.0f);
  private static final Animation outToRightAnimation = getAnimation(0.0f, 1.0f);

  private static Animation getAnimation(float fromX, float toX) {
    Animation anim = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, fromX,
            Animation.RELATIVE_TO_PARENT, toX,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f);
    anim.setDuration(300);
    anim.setInterpolator(new AccelerateInterpolator());
    return anim;
  }
}