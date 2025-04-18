/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.widget;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.transition.Transition;
import androidx.annotation.NonNull;

import com.google.android.material.transition.platform.MaterialSharedAxis;
import java.util.Objects;

import ir.ninjacoder.ghostide.R;
import io.github.rosemoe.sora.event.EventReceiver;
import io.github.rosemoe.sora.event.ScrollEvent;

/** Base class for all editor popup windows. */
public class EditorPopupWindow {

  /** Update the position of this window when user scrolls the editor */
  public static final int FEATURE_SCROLL_AS_CONTENT = 1;

  /**
   * Allow the window to be displayed outside the view's rectangle. Otherwise, the window's size
   * will be adjusted to force it to display in the view. If the space can't display it, it will get
   * hidden.
   */
  public static final int FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED = 1 << 1;

  /**
   * Hide this window when the user scrolls fast. Such as the selection handle is currently near the
   * edge of screen.
   */
  public static final int FEATURE_HIDE_WHEN_FAST_SCROLL = 1 << 2;

  private final PopupWindow mWindow;
  private final CodeEditor mEditor;
  private final int mFeatures;
  private final int[] mLocationBuffer = new int[2];
  private final EventReceiver<ScrollEvent> mScrollListener;
  private boolean mShowState;
  private boolean mRegisterFlag;
  private boolean mRegistered;
  private int mOffsetX, mOffsetY, mX, mY, mWidth, mHeight;

  /**
   * Create a popup window for editor
   *
   * @param features Features to request
   * @see #FEATURE_SCROLL_AS_CONTENT
   * @see #FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED
   * @see #FEATURE_HIDE_WHEN_FAST_SCROLL
   */
  public EditorPopupWindow(@NonNull CodeEditor editor, int features) {
    mEditor = Objects.requireNonNull(editor);
    mFeatures = features;
    mWindow = new PopupWindow(editor);
   ///   mWindow.setAnimationStyle(R.style.hso);
    mWindow.setElevation(editor.getDpUnit() * 8);
    customAnimByMaterial();
    mScrollListener =
        ((event, unsubscribe) -> {
          if (!mRegisterFlag) {
            unsubscribe.unsubscribe();
            mRegistered = false;
            return;
          }
          switch (event.getCause()) {
            case ScrollEvent.CAUSE_MAKE_POSITION_VISIBLE:
            case ScrollEvent.CAUSE_TEXT_SELECTING:
            case ScrollEvent.CAUSE_USER_FLING:
            case ScrollEvent.CAUSE_SCALE_TEXT:
              if (isFeatureEnabled(FEATURE_HIDE_WHEN_FAST_SCROLL)
                  && (Math.abs(event.getEndX() - event.getStartX()) > 80
                      || Math.abs(event.getEndY() - event.getStartY()) > 80)) {
                if (isShowing()) {
                  dismiss();
                  return;
                }
              }
              break;
          }
          if (isFeatureEnabled(FEATURE_SCROLL_AS_CONTENT)) {
            applyWindowAttributes(false);
          }
        });
    register();
  }

  /** Get editor instance */
  @NonNull
  public CodeEditor getEditor() {
    return mEditor;
  }

  /**
   * Checks whether a single feature is enabled
   *
   * @see #FEATURE_SCROLL_AS_CONTENT
   * @see #FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED
   * @see #FEATURE_HIDE_WHEN_FAST_SCROLL
   */
  public boolean isFeatureEnabled(int feature) {
    if (Integer.bitCount(feature) != 1) {
      throw new IllegalArgumentException("Not a valid feature integer");
    }
    return (mFeatures & feature) != 0;
  }

  /** Register this window in target editor. After registering, features are available. */
  public void register() {
    if (!mRegistered) {
      mEditor.subscribeEvent(ScrollEvent.class, mScrollListener);
    }
    mRegisterFlag = true;
  }

  /** Unregister this window in target editor. */
  public void unregister() {
    mRegisterFlag = false;
  }

  public boolean isShowing() {
    return mShowState;
  }

  public PopupWindow getPopup() {
    return mWindow;
  }

  public void setContentView(View view) {
    mWindow.setContentView(view);
  }

  private int wrapHorizontal(int horizontal) {
    return Math.max(0, Math.min(horizontal, mEditor.getWidth()));
  }

  private int wrapVertical(int vertical) {
    return Math.max(0, Math.min(vertical, mEditor.getHeight()));
  }

  private void applyWindowAttributes(boolean show) {
    if (!show && !isShowing()) {
      return;
    }
    boolean autoScroll = isFeatureEnabled(FEATURE_SCROLL_AS_CONTENT);
    var left = autoScroll ? (mX - mEditor.getOffsetX()) : (mX - mOffsetX);
    var top = autoScroll ? (mY - mEditor.getOffsetY()) : (mY - mOffsetY);
    var right = left + mWidth;
    var bottom = top + mHeight;
    if (!isFeatureEnabled(FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED)) {
      // Adjust positions
      left = wrapHorizontal(left);
      right = wrapHorizontal(right);
      top = wrapVertical(top);
      bottom = wrapVertical(bottom);
      if (top >= bottom || left >= right) {
        dismiss();
        return;
      }
    }
    // Show/update if needed
    mEditor.getLocationInWindow(mLocationBuffer);
    int width = right - left;
    int height = bottom - top;
    left += mLocationBuffer[0];
    top += mLocationBuffer[1];
    if (mWindow.isShowing()) {
      mWindow.update(left, top, width, height);
    } else if (show) {
      mWindow.setHeight(height);
      mWindow.setWidth(width);
      mWindow.showAtLocation(mEditor, Gravity.START | Gravity.TOP, left, top);
    }
  }

  public int getWidth() {
    return mWidth;
  }

  public int getHeight() {
    return mHeight;
  }

  /** Set the size of this window */
  public void setSize(int width, int height) {
    mWidth = width;
    mHeight = height;
    applyWindowAttributes(false);
  }

  /** Sets the position of the window <strong>in editor's drawing offset</strong> */
  public void setLocation(int x, int y) {
    mX = x;
    mY = y;
    mOffsetY = getEditor().getOffsetY();
    mOffsetX = getEditor().getOffsetX();
    applyWindowAttributes(false);
  }

  /** Sets the absolute position on view */
  public void setLocationAbsolutely(int x, int y) {
    setLocation(x + mEditor.getOffsetX(), y + mEditor.getOffsetY());
  }

  public void show() {
    if (mShowState) {
      return;
    }
    applyWindowAttributes(true);
    mShowState = true;
  }

  public void dismiss() {
    if (mShowState) {
      mShowState = false;
      mWindow.dismiss();
    }
  }

  void customAnimByMaterial() {
    Transition enter = new MaterialSharedAxis(MaterialSharedAxis.Z, true);
    enter.setDuration(200);
    mWindow.setEnterTransition(enter);
    Transition exit = new MaterialSharedAxis(MaterialSharedAxis.Z, false);
    exit.setDuration(200);
    mWindow.setExitTransition(exit);
    
  }
}
