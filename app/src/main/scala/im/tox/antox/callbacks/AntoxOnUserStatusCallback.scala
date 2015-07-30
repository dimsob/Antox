package im.tox.antox.callbacks

import android.content.Context
import im.tox.antox.data.{State, AntoxDB}
import im.tox.antox.tox.ToxSingleton
import im.tox.tox4j.core.callbacks.FriendStatusCallback
import im.tox.tox4j.core.enums.ToxUserStatus

object AntoxOnUserStatusCallback {

  private val TAG = "im.tox.antox.TAG"
}

class AntoxOnUserStatusCallback(private var ctx: Context) extends FriendStatusCallback {

  override def friendStatus (friendNumber: Int, status: ToxUserStatus): Unit = {
    val db = State.db
    db.updateContactStatus(ToxSingleton.tox.getFriendKey(friendNumber), status)
  }
}
