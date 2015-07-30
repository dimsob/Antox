package im.tox.antox.fragments

import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import im.tox.antox.adapters.ContactListAdapter
import im.tox.antox.utils.LeftPaneItem
import im.tox.antox.wrapper.{FriendInfo, FriendRequest, GroupInfo, GroupInvite}
import im.tox.antoxnightly.R
import im.tox.tox4j.core.enums.ToxUserStatus

class ContactsFragment extends AbstractContactsFragment(showSearch = true, showFab = true) {

  override def updateContacts(contactInfoTuple: (Seq[FriendInfo], Seq[FriendRequest],
    Seq[GroupInvite], Seq[GroupInfo])) {
    contactInfoTuple match {
      case (friendsList, friendRequests, groupInvites, groupList) =>
        leftPaneAdapter = new ContactListAdapter(getActivity)
        updateFriendsList(leftPaneAdapter, friendsList)
        updateFriendRequests(leftPaneAdapter, friendRequests)
        updateGroupInvites(leftPaneAdapter, groupInvites)
        updateGroupList(leftPaneAdapter, groupList)

        contactsListView.setAdapter(leftPaneAdapter)
    }
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val rootView = super.onCreateView(inflater, container, savedInstanceState)
    rootView.findViewById(R.id.center_text).setVisibility(View.GONE)
    rootView
  }

  def updateFriendsList(leftPaneAdapter: ContactListAdapter, friendsList: Seq[FriendInfo]): Unit = {
    val sortedFriendsList = friendsList.sortWith(compareNames).sortWith(compareOnline).sortWith(compareFavorite)
    if (sortedFriendsList.nonEmpty) {
      for (f <- sortedFriendsList) {
        val friend = new LeftPaneItem(f.key, f.avatar, f.getAliasOrName, f.statusMessage,
          f.online, f.getFriendStatusAsToxUserStatus, f.favorite, f.unreadCount,
          f.lastMessageTimestamp)
        leftPaneAdapter.addItem(friend)
      }
    }
  }

  def updateFriendRequests(leftPaneAdapter: ContactListAdapter, friendRequests: Seq[FriendRequest]): Unit = {
    if (friendRequests.nonEmpty) {
      for (r <- friendRequests) {
        val request = new LeftPaneItem(ContactItemType.FRIEND_REQUEST, r.requestKey, r.requestMessage)
        leftPaneAdapter.insert(0, request) // insert friend requests at top of contact list
      }
    }
  }

  def updateGroupInvites(leftPaneAdapter: ContactListAdapter, groupInvites: Seq[GroupInvite]): Unit = {
    if (groupInvites.nonEmpty) {
      for (invite <- groupInvites) {
        val request = new LeftPaneItem(ContactItemType.GROUP_INVITE, invite.groupKey, getResources.getString(R.string.invited_by) + " " + invite.inviter)
        leftPaneAdapter.addItem(request)
      }
    }
  }

  def updateGroupList(leftPaneAdapter: ContactListAdapter, groups: Seq[GroupInfo]): Unit = {
    val sortedGroupList = groups.sortWith(compareNames).sortWith(compareFavorite)
    if (groups.nonEmpty) {
      for (group <- groups) {
        val groupPane: LeftPaneItem = new LeftPaneItem(ContactItemType.GROUP, group.key, group.avatar, group.getAliasOrName, group.topic,
          group.online, ToxUserStatus.NONE, group.favorite, group.unreadCount, group.lastMessageTimestamp)
        leftPaneAdapter.addItem(groupPane)
      }
    }
  }
}
