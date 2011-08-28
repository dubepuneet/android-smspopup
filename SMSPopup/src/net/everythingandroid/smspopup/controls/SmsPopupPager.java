package net.everythingandroid.smspopup.controls;

import java.util.ArrayList;

import net.everythingandroid.smspopup.Log;
import net.everythingandroid.smspopup.SmsMmsMessage;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;

public class SmsPopupPager extends ViewPager implements OnPageChangeListener {

  private ArrayList<SmsMmsMessage> messages;
  private int currentPage;
  private MessageCountChanged messageCountChanged;
  private Context mContext;
  private SmsPopupPagerAdapter mAdapter;

  public SmsPopupPager(Context context) {
    super(context);
    init(context);
  }

  public SmsPopupPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    mContext = context;
    messages = new ArrayList<SmsMmsMessage>(5);
    mAdapter = new SmsPopupPagerAdapter();
    setAdapter(mAdapter);
    currentPage = 0;
    setOnPageChangeListener(this);
  }

  public int getPageCount() {
    return messages.size();
  }

  /**
   * Add a message and its view to the end of the list of messages.
   * 
   * @param newMessage
   *          The message to add.
   */
  public void addMessage(SmsMmsMessage newMessage) {
    messages.add(newMessage);
    UpdateMessageCount();
  }

  /**
   * Add a list of new messages to the end of the current message list.
   * 
   * @param newMessages
   *          The list of new messages to add.
   */
  public void addMessages(ArrayList<SmsMmsMessage> newMessages) {
    if (newMessages != null) {
      messages.addAll(newMessages);
      UpdateMessageCount();
    }
  }

  /**
   * Remove a specific message from the list, if there is only one message left
   * then it will not be removed.
   * 
   * @param numMessage
   * @return true if a message was removed, false otherwise.
   */
  public boolean removeMessage(int numMessage) {
    final int totalMessages = getPageCount();

    if (totalMessages <= 1)
      return false;

    if (numMessage >= totalMessages || numMessage < 0)
      return false;
    
    // messageToDelete = currentPage;

    if (currentPage == numMessage) {
      // If removing last page, go to previous
      if (currentPage == (totalMessages - 1)) {
        showPrevious();
      } else {
        showNext();
      }
    }

    // messages.remove(numMessage);
    //
    // // if (showNext) {
    // // showPrevious();
    // // }
    //
    // messageToDelete
    // mAdapter.notifyDataSetChanged();

    // invalidate();

    // if (savedCurrentPage == numMessage) {
    // // If removing last page, go to previous
    // if (savedCurrentPage == (totalMessages - 1)) {
    // showPrevious();
    // } else {
    // showNext();
    // }
    // }
    //
    // // Remove message from list
    // messages.remove(numMessage);
    //
    // if (numMessage <= savedCurrentPage) {
    // currentPage--;
    // }
    //
    // setCurrentItem(savedCurrentPage);
    //
    // mAdapter.notifyDataSetChanged();
    //
    // // Run any other updates (as set by interface)
    // UpdateMessageCount();

    return true;
  }

  /**
   * Remove the currently active message, if there is only one message left then
   * it will not be removed.
   * 
   * @return true if a message was removed, false otherwise.
   */
  public boolean removeActiveMessage() {
    return removeMessage(currentPage);
  }

  /**
   * Return the currently active message.
   * 
   * @return
   */
  public SmsMmsMessage getActiveMessage() {
    return messages.get(currentPage);
  }

  public void setOnMessageCountChanged(MessageCountChanged m) {
    messageCountChanged = m;
  }

  public static interface MessageCountChanged {
    abstract void onChange(int current, int total);
  }

  private void UpdateMessageCount() {
    if (messageCountChanged != null) {
      messageCountChanged.onChange(currentPage, getPageCount());
    }
  }

  public void showNext() {
    if (currentPage < (getPageCount() - 1)) {
      setCurrentItem(currentPage + 1);
    }
    if (Log.DEBUG)
      Log.v("showNext() - " + currentPage + ", " + getActiveMessage().getContactName());
  }

  public void showPrevious() {
    if (currentPage > 0) {
      setCurrentItem(currentPage - 1);
    }
    if (Log.DEBUG)
      Log.v("showPrevious() - " + currentPage + ", " + getActiveMessage().getContactName());
  }

  @Override
  public void setCurrentItem(int num) {
    super.setCurrentItem(num);
    UpdateMessageCount();
  }

  @Override
  public void onPageScrollStateChanged(int state) {
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
  }

  @Override
  public void onPageSelected(int position) {
    currentPage = position;
    UpdateMessageCount();
    Log.v("onPageSelected - " + currentPage);
  }

  private class SmsPopupPagerAdapter extends PagerAdapter {

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public int getCount() {
      return getPageCount();
    }

    @Override
    public Object instantiateItem(View container, int position) {
      SmsPopupView mView = new SmsPopupView(mContext, messages.get(position));
      ((ViewPager) container).addView(mView);
      return mView;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
      ((ViewPager) container).removeView((SmsPopupView) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == ((SmsPopupView) object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
      return null;
    }

    @Override
    public void startUpdate(View container) {
    }
  }
}