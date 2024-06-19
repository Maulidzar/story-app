package com.example.storyapp.UI.Authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.example.storyapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val authViewModel: AuthenViewModel by activityViewModels {
        AuthViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        playAnimation()
    }

    private fun toLogin(){
        val mLoginFragment = LoginFragment()
        val mFragmentManager = parentFragmentManager
        mFragmentManager.popBackStack()
        mFragmentManager.commit {
            replace(R.id.frame_authen, mLoginFragment, LoginFragment::class.java.simpleName)
        }
    }

    private fun setupAction(){
        binding.btnDaftar.setOnClickListener {
            val name = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8){
                binding.apply {
                    etNama.onEditorAction(EditorInfo.IME_ACTION_DONE)
                    etEmail.onEditorAction(EditorInfo.IME_ACTION_DONE)
                    etPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
                }
                authViewModel.register(name, email, password).observe(requireActivity()){ result ->
                    when(result){
                        is StoryResult.isLoading ->{
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is StoryResult.isSuccess ->{
                            binding.progressBar.visibility = View.GONE
                            toLogin()
                            Toast.makeText(requireActivity(), getString(R.string.success_register), Toast.LENGTH_SHORT).show()
                        }
                        is StoryResult.isError ->{
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(requireActivity(), getString(R.string.invalid_register), Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnLogin.setOnClickListener { toLogin() }
    }

    private fun playAnimation() {
        binding.apply {
            val welcome = ObjectAnimator.ofFloat(textView, View.ALPHA, 1f).setDuration(275)
            val name = ObjectAnimator.ofFloat(etNama, View.ALPHA, 1f).setDuration(275)
            val email = ObjectAnimator.ofFloat(etEmail, View.ALPHA, 1f).setDuration(275)
            val password = ObjectAnimator.ofFloat(etPassword, View.ALPHA, 1f).setDuration(275)
            val register = ObjectAnimator.ofFloat(btnDaftar, View.ALPHA, 1f).setDuration(275)
            val btnToLogin = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(275)
            val haveAccount = ObjectAnimator.ofFloat(textView2, View.ALPHA, 1f).setDuration(275)

            AnimatorSet().apply {
                playSequentially(
                    welcome,
                    name,
                    email,
                    password,
                    register,
                    haveAccount,
                    btnToLogin,
                )
                start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}