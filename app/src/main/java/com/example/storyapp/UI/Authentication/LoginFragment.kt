package com.example.storyapp.UI.Authentication

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.storyapp.Data.StoryData.StoryResult
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private val authViewModel: AuthenViewModel by activityViewModels {
        AuthViewModelFactory.getInstance(requireActivity().dataStore)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction(){

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8){
                binding.apply {
                    etEmail.onEditorAction(EditorInfo.IME_ACTION_DONE)
                    etPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
                }
                authViewModel.login(email, password).observe(requireActivity()){result ->
                    when(result){
                        is StoryResult.isLoading ->{
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is StoryResult.isSuccess ->{
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), getString(R.string.success_login), Toast.LENGTH_SHORT).show()
                        }
                        is StoryResult.isError ->{
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireActivity(), getString(R.string.invalid_login), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(requireActivity(), getString(R.string.invalid_login), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDaftar.setOnClickListener {
            val mRegisterFragment = RegisterFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.frame_authen, mRegisterFragment, RegisterFragment::class.java.simpleName)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}