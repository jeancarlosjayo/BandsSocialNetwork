package com.jramdev.bandssocialnetwork

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class ChatsFragment : Fragment() {
    var firebaseAuth: FirebaseAuth? = null
    var recyclerViewBanda: RecyclerView? = null
    var recyclerViewCliente: RecyclerView? = null
    var recyclerViewPromotor: RecyclerView? = null
    var chatListLista: List<ChatLista>? = null
    var userBandaList: List<BandaUser>? = null
    var userClienteList: List<ClienteUser>? = null
    var userPromotorList: List<PromotorUser>? = null
    private var reference: DatabaseReference? = null
    var currentUser: FirebaseUser? = null
    var adapterChatBandaList: AdapterChatBandaList? = null
    var adapterChatClienteList: AdapterChatClienteList? = null
    var adapterChatPromotorList: AdapterChatPromotorList? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chats, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser
        recyclerViewBanda = view.findViewById(R.id.recyclerviewchatBanda)
        recyclerViewCliente = view.findViewById(R.id.recyclerviewchatCliente)
        recyclerViewPromotor = view.findViewById(R.id.recyclerviewchatPromotor)
        chatListLista = ArrayList()
        reference =
            FirebaseDatabase.getInstance().getReference("ChatLista").child(currentUser!!.uid)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (chatListLista as ArrayList<ChatLista>).clear()
                for (ds in dataSnapshot.children) {
                    val chatLista = ds.getValue(ChatLista::class.java)
                    (chatListLista as ArrayList<ChatLista>).add(chatLista!!)
                }
                loadChatsTypeBanda()
                loadChatsTypeCliente()
                loadChatsTypePromotor()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return view
    }

    private fun loadChatsTypeBanda() {
        userBandaList = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("users")
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (userBandaList as ArrayList<BandaUser>).clear()
                for (ds in dataSnapshot.children) {
                    val bandaUser: BandaUser? = ds.getValue(BandaUser::class.java)
                    for (chatlist in chatListLista!!) {
                        if (bandaUser != null) {
                            if (bandaUser.getUid() != null && bandaUser.getUid()
                                    .equals(chatlist.getId()) && bandaUser.getTypeuser()
                                    .equals("banda")
                            ) {
                                (userBandaList as ArrayList<BandaUser>).add(bandaUser)
                                break
                            }
                        }
                    }
                    //adapter
                    adapterChatBandaList =
                        AdapterChatBandaList(context, userBandaList)
                    recyclerViewBanda!!.adapter = adapterChatBandaList
                    //setadapter
                    //set last message
                    for (i in userBandaList!!.indices) {
                        lastMessageBanda(userBandaList!![i].getUid())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //Group Chats
    }

    private fun loadChatsTypeCliente() {
        userClienteList = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("users")
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (userClienteList as ArrayList<ClienteUser>).clear()
                for (ds in dataSnapshot.children) {
                    val clienteUser: ClienteUser? = ds.getValue(ClienteUser::class.java)
                    for (chatlist in chatListLista!!) {
                        if (clienteUser != null) {
                            if (clienteUser.getUid() != null && clienteUser.getUid()
                                    .equals(chatlist.getId()) && clienteUser.getTypeuser()
                                    .equals("cliente")
                            ) {
                                (userClienteList as ArrayList<ClienteUser>).add(clienteUser)
                                break
                            }
                        }
                    }
                    //adapter
                    adapterChatClienteList =
                        AdapterChatClienteList(context, userClienteList)
                    recyclerViewCliente!!.adapter = adapterChatClienteList
                    //setadapter
                    //set last message
                    for (i in userClienteList!!.indices) {
                        lastMessageCliente(userClienteList!![i].getUid())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //Group Chats
    }

    private fun loadChatsTypePromotor() {
        userPromotorList = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("users")
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (userPromotorList as ArrayList<PromotorUser>).clear()
                for (ds in dataSnapshot.children) {
                    val PromotorUser: PromotorUser? = ds.getValue(PromotorUser::class.java)
                    for (chatlist in chatListLista!!) {
                        if (PromotorUser != null) {
                            if (PromotorUser.getUid() != null && PromotorUser.getUid()
                                    .equals(chatlist.getId()) && PromotorUser.getTypeuser()
                                    .equals("promotor")
                            ) {
                                (userPromotorList as ArrayList<PromotorUser>).add(PromotorUser)
                                break
                            }
                        }
                    }
                    //adapter
                    adapterChatPromotorList =
                        AdapterChatPromotorList(context, userPromotorList)
                    recyclerViewPromotor!!.adapter = adapterChatPromotorList
                    //setadapter
                    //set last message
                    for (i in userPromotorList!!.indices) {
                        lastMessagePromotor(userPromotorList!![i].getUid())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //Group Chats
    }

    private fun lastMessageBanda(userid: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var thelastMessage: String = "default"
                for (ds in dataSnapshot.children) {
                    val chat = ds.getValue(Chat::class.java) ?: continue
                    val sender = chat.getEmisor()
                    val receiver = chat.getReceptor()
                    if (sender == null || receiver == null) {
                        continue
                    }
                    if (chat.getReceptor().equals(currentUser!!.uid) &&
                        chat.getEmisor().equals(userid) || chat.getReceptor()
                            .equals(userid) && chat.getEmisor().equals(
                            currentUser!!.uid
                        )
                    ) {
                        thelastMessage = chat.getMensaje()
                    }
                }
                adapterChatBandaList?.setLastMessageMap(userid, thelastMessage)
                adapterChatBandaList?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun lastMessageCliente(userid: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var thelastMessage: String = "default"
                for (ds in dataSnapshot.children) {
                    val chat = ds.getValue(Chat::class.java) ?: continue
                    val sender = chat.getEmisor()
                    val receiver = chat.getReceptor()
                    if (sender == null || receiver == null) {
                        continue
                    }
                    if (chat.getReceptor().equals(currentUser!!.uid) &&
                        chat.getEmisor().equals(userid) || chat.getReceptor()
                            .equals(userid) && chat.getEmisor().equals(
                            currentUser!!.uid
                        )
                    ) {
                        thelastMessage = chat.getMensaje()
                    }
                }
                adapterChatClienteList?.setLastMessageMap(userid, thelastMessage)
                adapterChatClienteList?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun lastMessagePromotor(userid: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var thelastMessage: String = "default"
                for (ds in dataSnapshot.children) {
                    val chat = ds.getValue(Chat::class.java) ?: continue
                    val sender = chat.getEmisor()
                    val receiver = chat.getReceptor()
                    if (sender == null || receiver == null) {
                        continue
                    }
                    if (chat.getReceptor().equals(currentUser!!.uid) &&
                        chat.getEmisor().equals(userid) || chat.getReceptor()
                            .equals(userid) && chat.getEmisor().equals(
                            currentUser!!.uid
                        )
                    ) {
                        thelastMessage = chat.getMensaje()
                    }
                }
                adapterChatPromotorList?.setLastMessageMap(userid, thelastMessage)
                adapterChatPromotorList?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun checkUserStatus() {
        val user = firebaseAuth!!.currentUser
        if (user != null) {
            //user is signed in stay here
//set email of logged in user
            //mProfileTV.setText(user.getEmail());
        } else {
            //user is signed in stay here
            startActivity(Intent(activity, MainActivity::class.java))
            requireActivity().finish()
        }
    }

}