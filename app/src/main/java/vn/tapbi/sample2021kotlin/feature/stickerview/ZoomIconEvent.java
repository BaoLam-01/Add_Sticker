package vn.tapbi.sample2021kotlin.feature.stickerview;

import android.view.MotionEvent;

/**
 * @author wupanjie
 */

public class ZoomIconEvent implements StickerIconEvent {
  @Override public void onActionDown(StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionMove(StickerView stickerView, MotionEvent event) {
    stickerView.zoomAndRotateCurrentSticker(event);
  }

  @Override public void onActionUp(StickerView stickerView, MotionEvent event) {
    if (stickerView.getOnStickerOperationListener() != null) {
      stickerView.getOnStickerOperationListener()
          .onStickerZoomFinished(stickerView.getCurrentSticker());
    }
  }
}
