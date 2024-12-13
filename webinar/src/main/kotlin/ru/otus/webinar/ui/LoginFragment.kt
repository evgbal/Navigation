package ru.otus.webinar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.otus.webinar.R
import ru.otus.webinar.data.sessionManager
import ru.otus.webinar.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), DialogListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val sessionManager by sessionManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            login.setOnClickListener {
                login()
            }
        }
    }

    private fun login() = lifecycleScope.launch {
        try {
            sessionManager.login(
                binding.loginInput.text.toString(),
                binding.passwordInput.text.toString()
            )
            parentFragmentManager.commit {
                replace(R.id.fragment_container_view, TabsFragment())
            }
        } catch (e: Exception) {
            ErrorDialogFragment.newInstance(e.message.orEmpty()).show(
                childFragmentManager,
                ErrorDialogFragment.TAG
            )
        }
    }

    override fun onDialogDismiss(tag: String) {
        if (tag == ErrorDialogFragment.TAG) {
            parentFragmentManager.commit {
                replace(R.id.fragment_container_view, WelcomeFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}