package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() , Toolbar.OnMenuItemClickListener {
    lateinit  var repoList:ArrayList<RepoModel>
    lateinit  var api:String
    override fun onCreate(savedInstanceState: Bundle?) {
        api = "https://api.github.com/"
//        loadData()

//


        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        loadData()
//        if(repoList.isEmpty())
//            repoList = arrayListOf()
        val materialToolbar: MaterialToolbar = findViewById(R.id.material_toolbar)
        materialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    Toast.makeText(this, "Favorites Clicked", Toast.LENGTH_SHORT).show()
                    getRepoAlertBox()
                    true
                }

                else -> false
            }
        }





    }

    override fun onPause() {
        Log.i("pref","saving data")
        if(repoList.isNotEmpty()){
            Log.i("pref",repoList.toString())
            val sharedPreferences = getSharedPreferences("RepoList", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
           val  gson = Gson()
            val json = gson.toJson(repoList)
            editor.putString("Repos",json)
            editor.apply()
        }
        super.onPause()
    }


     fun loadData() {
         Log.i("pref","getting data")
        val sharedPreferences = getSharedPreferences("RepoList", MODE_PRIVATE)
        val  gson = Gson()
        val json = sharedPreferences.getString("Repos","[]")
        val type: Type = object :TypeToken<ArrayList<RepoModel>>(){}.type
         if(json != null)
            repoList = gson.fromJson(json,type)
         else
             repoList = arrayListOf()

         buildRV(repoList)


    }

    private fun getRepoAlertBox() {
        val  builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.activity_repo_details,null)
        val owner:EditText = dialogLayout.findViewById(R.id.etOwner)
        val repo:EditText = dialogLayout.findViewById(R.id.etRepo)
        with(builder){
            setTitle("Enter Repo")
            setPositiveButton("Add"){
                dialog,which->
                getData(owner.text.toString(), repo.text.toString())

            }
            setView(dialogLayout)
            show()
        }
    }


    fun buildRV(repoList:List<RepoModel>){
        val rvList: RecyclerView= findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)
        val adapter = CustomAdapter(this,repoList)
        rvList.adapter = adapter
    }
    fun getData(owner:String,repo:String){
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(api)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData  = retrofitBuilder.getRepo(owner,repo)



        retrofitData.enqueue(object : Callback<RepoModel?> {
            override fun onResponse(call: Call<RepoModel?>, response: Response<RepoModel?>) {
                Log.i("REs",response.body().toString())
                val modal: RepoModel? = response.body()!!
                Log.i("REs",modal!!.name)
                Log.i("Res", "response 33: " + modal.url+repoList.size)
                repoList.add(RepoModel(modal.name,modal.description,modal.url))
                buildRV(repoList)

            }

            override fun onFailure(call: Call<RepoModel?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })



//        retrofitData.enqueue(object :Callback<repoModel?> {
//            //Callback<List<repoModel>?> {
//            override fun onResponse(
//                call: Call<repoModel?>,
//                response: Response<repoModel?>
//            ) {
//                Toast.makeText(this@MainActivity,"YOKLO",Toast.LENGTH_SHORT).show()
//
//                val resBody = response.body()!!
//                Toast.makeText(this@MainActivity,resBody.toString(),Toast.LENGTH_SHORT).show()
//                val mystringBuilder = StringBuilder()
//                for(myData in resBody){
//                    mystringBuilder.append(myData.name)
//                }
////                Toast.makeText(this@MainActivity,resBody.toString(),Toast.LENGTH_SHORT).show()
//                Log.i("Res",mystringBuilder.toString())
//            }
//
//            override fun onFailure(call: Call<repoModel?>, t: Throwable) {
//               Log.e("Res","Error")
//            }
//        })


    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        Toast.makeText(this, "Menu Item issdcs Pressed", Toast.LENGTH_SHORT).show()
        return true
    }
}