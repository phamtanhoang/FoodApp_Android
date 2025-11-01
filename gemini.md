--- E:\App\FoodApp_Android\app\src\androidTest\java\com\pth\androidapp\ExampleInstrumentedTest.kt ---

package com.pth.androidapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.pth.androidapp", appContext.packageName)
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\MyApplication.kt ---

package com.pth.androidapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}


--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\base\activities\BaseActivity.kt ---

package com.pth.androidapp.core.base.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.max
import androidx.viewbinding.ViewBinding
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.ConfirmDialogFragment
import com.pth.androidapp.core.base.dialogs.LoadingDialogFragment
import com.pth.androidapp.core.base.dialogs.NotifyDialogFragment
import com.pth.androidapp.core.base.dialogs.NotifyType
import com.pth.androidapp.core.common.LanguageManager

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
        private set

    abstract fun inflateBinding(inflater: LayoutInflater): VB

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val languageManager = LanguageManager(this)
        languageManager.setLanguage(languageManager.getUserSelectedLanguageCode())

        super.onCreate(savedInstanceState)

        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
    }

    open fun restart() {
        recreate()
    }

    open fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomInset = max(systemBars.bottom, imeInsets.bottom)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomInset)
            insets
        }
    }

    open fun navigateToActivity(
        activityClass: Class<out Activity>,
        extras: Bundle? = null,
        flags: Int? = null,
        finishCurrent: Boolean = false
    ) {
        val intent = Intent(this, activityClass)
        extras?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        startActivity(intent)
        if (finishCurrent) {
            finish()
        }
    }

    open fun launchActivityForResult(
        activityClass: Class<out Activity>,
        extras: Bundle? = null,
        flags: Int? = null
    ) {
        val intent = Intent(this, activityClass)
        extras?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        activityResultLauncher.launch(intent)
    }

    protected open fun handleActivityResult(result: ActivityResult) {

    }

    open fun showLoading(isShow: Boolean, message: String? = null) {
        if (isShow) {
            LoadingDialogFragment.show(supportFragmentManager, message)
        } else {
            LoadingDialogFragment.hide()
        }
    }

    open fun showNotifyDialog(
        type: NotifyType,
        message: String,
        textButton: String? = null,
        callback: () -> Unit = {}
    ) {
        NotifyDialogFragment.Builder(this)
            .type(type)
            .message(message)
            .buttonText(textButton ?: getString(R.string.ok))
            .onOk(callback)
            .build()
            .show(supportFragmentManager, NotifyDialogFragment.TAG)
    }

    open fun showConfirmDialog(
        title: String,
        message: String?,
        positiveButtonTitle: String? = null,
        negativeButtonTitle: String? = null,
        callbackNegative: () -> Unit = {},
        callbackPositive: () -> Unit = {}
    ) {
        val builder = ConfirmDialogFragment.Builder(this)
            .title(title)
            .onNegative(callbackNegative)
            .onPositive(callbackPositive)

        message?.let { builder.message(it) }
        positiveButtonTitle?.let { builder.positiveButtonTitle(it) }
        negativeButtonTitle?.let { builder.negativeButtonTitle(it) }

        builder.build().show(supportFragmentManager, ConfirmDialogFragment.TAG)
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\base\dialogs\ConfirmDialogFragment.kt ---

package com.pth.androidapp.core.base.dialogs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.pth.androidapp.R
import com.pth.androidapp.databinding.DialogConfirmBinding
import androidx.core.graphics.drawable.toDrawable

class ConfirmDialogFragment : DialogFragment() {
    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_MESSAGE = "arg_message"
        private const val ARG_POSITIVE_TITLE = "arg_positive_title"
        private const val ARG_NEGATIVE_TITLE = "arg_negative_title"

        const val TAG = "ConfirmDialogFragment"
    }
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private var onPositiveClick: () -> Unit = {}
    private var onNegativeClick: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding.apply {
            val title = arguments?.getString(ARG_TITLE)
            val message = arguments?.getString(ARG_MESSAGE)
            val positiveButtonTitle = arguments?.getString(ARG_POSITIVE_TITLE)
            val negativeButtonTitle = arguments?.getString(ARG_NEGATIVE_TITLE)

            tvTitle.text = title
            tvContent.isVisible = !message.isNullOrEmpty()
            tvContent.text = message
            btnPositive.text = positiveButtonTitle
            btnNegative.text = negativeButtonTitle
        }
    }

    private fun setupListeners() {
        binding.btnPositive.setOnClickListener {
            onPositiveClick.invoke()
            dismiss()
        }
        binding.btnNegative.setOnClickListener {
            onNegativeClick.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
            dialog?.window?.setLayout(
            resources.displayMetrics.widthPixels - 2 * (16 * resources.displayMetrics.density).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    class Builder(private val context: Context) {
        private var title: String = ""
        private var message: String? = null
        private var positiveButtonTitle: String? = null
        private var negativeButtonTitle: String? = null
        private var onPositive: () -> Unit = {}
        private var onNegative: () -> Unit = {}

        fun title(title: String) = apply { this.title = title }
        fun message(message: String) = apply { this.message = message }
        fun positiveButtonTitle(title: String) = apply { this.positiveButtonTitle = title }
        fun negativeButtonTitle(title: String) = apply { this.negativeButtonTitle = title }
        fun onPositive(action: () -> Unit) = apply { this.onPositive = action }
        fun onNegative(action: () -> Unit) = apply { this.onNegative = action }

        fun build(): ConfirmDialogFragment {
            val fragment = ConfirmDialogFragment()
            fragment.arguments = bundleOf(
                ARG_TITLE to title,
                ARG_MESSAGE to message,
                ARG_POSITIVE_TITLE to (positiveButtonTitle ?: context.getString(R.string.ok)),
                ARG_NEGATIVE_TITLE to (negativeButtonTitle ?: context.getString(R.string.cancel))
            )
            fragment.onPositiveClick = onPositive
            fragment.onNegativeClick = onNegative
            return fragment
        }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\base\dialogs\LoadingDialogFragment.kt ---

package com.pth.androidapp.core.base.dialogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pth.androidapp.R
import com.pth.androidapp.databinding.DialogLoadingBinding
import androidx.core.graphics.drawable.toDrawable

class LoadingDialogFragment : DialogFragment() {
    private var _binding: DialogLoadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLoadingBinding.inflate(inflater, container, false)
        // Đảm bảo nền của cửa sổ dialog trong suốt
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog?.window?.setDimAmount(0f) // Loại bỏ lớp nền mờ phía sau
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        val message = arguments?.getString(ARG_MESSAGE)

        // Hiển thị message nếu có, nếu không thì dùng string mặc định
        binding.tvLoadingMessage.text = message ?: getString(R.string.loading)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LoadingDialogFragment"
        private const val ARG_MESSAGE = "arg_message"

        @Volatile
        private var instance: LoadingDialogFragment? = null

        fun show(fragmentManager: FragmentManager, message: String? = null) {
            // Chỉ tạo và hiển thị nếu chưa có instance nào
            if (instance == null) {
                // Sử dụng synchronized để đảm bảo an toàn trên nhiều luồng
                synchronized(this) {
                    if (instance == null) {
                        if (fragmentManager.isStateSaved) return // Không hiển thị nếu state đã được lưu

                        instance = LoadingDialogFragment().apply {
                            arguments = bundleOf(ARG_MESSAGE to message)
                        }
                        try {
                            instance?.show(fragmentManager, TAG)
                        } catch (e: IllegalStateException) {
                            // Bỏ qua lỗi nếu không thể thêm fragment
                        }
                    }
                }
            }
        }

        fun hide() {
            try {
                instance?.dismissAllowingStateLoss()
            } catch (e: Exception) {
                // Bỏ qua lỗi
            } finally {
                instance = null
            }
        }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\base\dialogs\NotifyDialogFragment.kt ---

package com.pth.androidapp.core.base.dialogs

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.color.MaterialColors
import com.pth.androidapp.R
import com.pth.androidapp.databinding.DialogNotifyBinding
import androidx.core.graphics.drawable.toDrawable

enum class NotifyType {
    SUCCESS, WARNING, ERROR, INFO
}

@Suppress("DEPRECATION")
class NotifyDialogFragment : DialogFragment() {
    companion object {
        private const val ARG_TYPE = "arg_type"
        private const val ARG_MESSAGE = "arg_message"
        private const val ARG_BUTTON_TEXT = "arg_button_text"

        const val TAG = "NotifyDialogFragment"
    }

    private var _binding: DialogNotifyBinding? = null
    private val binding get() = _binding!!

    private var onOkClick: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNotifyBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            resources.displayMetrics.widthPixels - 2 * (16 * resources.displayMetrics.density).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        val type = arguments?.getSerializable(ARG_TYPE) as? NotifyType ?: NotifyType.INFO
        val message = arguments?.getString(ARG_MESSAGE)
        val buttonText = arguments?.getString(ARG_BUTTON_TEXT)

        binding.tvContent.text = message
        binding.btnOK.text = buttonText ?: getString(R.string.ok)

        when (type) {
            NotifyType.SUCCESS -> {
                binding.tvTitle.text = getString(R.string.success)
                binding.ivIcon.setImageResource(R.drawable.ic_success)
            }
            NotifyType.WARNING -> {
                binding.tvTitle.text = getString(R.string.warning)
                binding.ivIcon.setImageResource(R.drawable.ic_warning)
            }
            NotifyType.ERROR -> {
                binding.tvTitle.text = getString(R.string.error)
                binding.ivIcon.setImageResource(R.drawable.ic_error)
            }
            NotifyType.INFO -> {
                binding.tvTitle.text = getString(R.string.information)
                binding.ivIcon.setImageResource(R.drawable.ic_info)
            }
        }
    }

    private fun setupListeners() {
        binding.btnOK.setOnClickListener {
            onOkClick.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @ColorInt
    private fun getThemeColor(@AttrRes attrResId: Int): Int {
        return MaterialColors.getColor(requireContext(), attrResId, Color.BLACK)
    }

    class Builder(private val context: Context) {
        private var type: NotifyType = NotifyType.INFO
        private var message: String = ""
        private var buttonText: String? = null
        private var onOk: () -> Unit = {}

        fun type(type: NotifyType) = apply { this.type = type }
        fun message(message: String) = apply { this.message = message }
        fun buttonText(text: String) = apply { this.buttonText = text }
        fun onOk(action: () -> Unit) = apply { this.onOk = action }

        fun build(): NotifyDialogFragment {
            val fragment = NotifyDialogFragment()
            fragment.arguments = bundleOf(
                ARG_TYPE to type,
                ARG_MESSAGE to message,
                ARG_BUTTON_TEXT to buttonText
            )
            fragment.onOkClick = onOk
            return fragment
        }
    }

}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\base\fragments\BaseFragment.kt ---

package com.pth.androidapp.core.base.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.ConfirmDialogFragment
import com.pth.androidapp.core.base.dialogs.LoadingDialogFragment
import com.pth.androidapp.core.base.dialogs.NotifyDialogFragment
import com.pth.androidapp.core.base.dialogs.NotifyType

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun navigateToPage(actionId: Int, args: Bundle? = null) {
        findNavController().navigate(actionId, args)
    }

    protected fun navigateToPageAndClearBackStack(actionId: Int, navGraphId: Int, args: Bundle? = null) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navGraphId, true)
            .build()
        findNavController().navigate(actionId, args, navOptions)
    }

    protected fun navigateToActivity(
        activityClass: Class<out Activity>,
        extras: Bundle? = null,
        flags: Int? = null,
        finishCurrent: Boolean = false
    ) {
        val intent = Intent(requireActivity(), activityClass)
        extras?.let { intent.putExtras(it) }
        flags?.let { intent.flags = it }
        startActivity(intent)
        if (finishCurrent) {
            requireActivity().finish()
        }
    }

    protected fun launchActivityForResult(activityClass: Class<out Activity>, extras: Bundle? = null) {
        val intent = Intent(requireActivity(), activityClass)
        extras?.let { intent.putExtras(it) }
        activityResultLauncher.launch(intent)
    }

    protected open fun handleActivityResult(result: ActivityResult) {
        // Mặc định không làm gì
    }

    protected fun navigateToLogin(loginActivityClass: Class<out Activity>) {
        val intent = Intent(requireActivity(), loginActivityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    protected fun showLoading(isShow: Boolean, message: String? = null) {
        if (isShow) {
            LoadingDialogFragment.show(parentFragmentManager, message)
        } else {
            LoadingDialogFragment.hide()
        }
    }

    protected fun showNotifyDialog(
        type: NotifyType,
        message: String,
        textButton: String? = null,
        callback: () -> Unit = {}
    ) {
        NotifyDialogFragment.Builder(requireContext())
            .type(type)
            .message(message)
            .buttonText(textButton ?: getString(R.string.ok))
            .onOk(callback)
            .build()
            .show(parentFragmentManager, NotifyDialogFragment.TAG)
    }

    protected fun showConfirmDialog(
        title: String,
        message: String?,
        positiveButtonTitle: String? = null,
        negativeButtonTitle: String? = null,
        callbackNegative: () -> Unit = {},
        callbackPositive: () -> Unit = {}
    ) {
        val builder = ConfirmDialogFragment.Builder(requireContext())
            .title(title)
            .onNegative(callbackNegative)
            .onPositive(callbackPositive)

        message?.let { builder.message(it) }
        positiveButtonTitle?.let { builder.positiveButtonTitle(it) }
        negativeButtonTitle?.let { builder.negativeButtonTitle(it) }

        builder.build().show(parentFragmentManager, ConfirmDialogFragment.TAG)
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\base\viewmodels\BaseViewModel.kt ---

package com.pth.androidapp.core.base.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    protected fun <T> execute(
        stateFlow: MutableStateFlow<UiState<T>>,
        block: suspend () -> T
    ) {
        viewModelScope.launch {
            stateFlow.value = UiState.Loading
            try {
                val result = block()
                stateFlow.value = UiState.Success(result)
            } catch (e: Exception) {
                stateFlow.value = UiState.Error(message = e.message ?: e.toString())
            }
        }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\common\BindingAdapters.kt ---

package com.pth.androidapp.core.common

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.MutableStateFlow

@BindingAdapter("stateFlowText")
fun bindStateFlowText(editText: TextInputEditText, state: MutableStateFlow<com.pth.androidapp.core.common.TextFieldState>?) {
    if (state == null) return
    if (editText.tag != state) {
        editText.setText(state.value.text)
        editText.tag = state
    }
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (state.value.text != s.toString()) {
                state.value = state.value.copy(text = s.toString())
            }
        }
        override fun afterTextChanged(s: Editable?) {}
    })
}


--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\common\InputValidator.kt ---

package com.pth.androidapp.core.common

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InputValidator @Inject constructor() {

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    fun validatePassword(password: String): Boolean {
        return !(password.length < 8 || !password.contains(Regex("[A-Z]")) || !password.contains(Regex("[a-z]")) || !password.contains(
            Regex("[0-9]")
        ) || password.matches(Regex("^[a-zA-Z0-9]+\$")))
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\common\LanguageManager.kt ---

package com.pth.androidapp.core.common

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import com.pth.androidapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "app_language_prefs"
        private const val KEY_SELECTED_LANGUAGE = "selected_language_choice"
    }

    data class LanguageItem(val code: String, val displayName: String)

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getAvailableLanguages(): List<LanguageItem> {
        return listOf(
            LanguageItem("en", context.getString(R.string.english)),
            LanguageItem("vi", context.getString(R.string.vietnamese)),
        )
    }

    fun getUserSelectedLanguageCode(): String {
        return sharedPrefs.getString(KEY_SELECTED_LANGUAGE, getSystemLanguage())!!
    }

    fun getAppLanguageCode(): String {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        return if (!appLocales.isEmpty) {
            appLocales[0]?.language ?: getUserSelectedLanguageCode()
        } else {
            getUserSelectedLanguageCode()
        }
    }

    fun getCurrentLanguagePosition(): Int {
        val currentCode = getAppLanguageCode()
        return getAvailableLanguages().indexOfFirst { it.code == currentCode }.coerceAtLeast(0)
    }

    fun setLanguage(languageCode: String) {
        sharedPrefs.edit { putString(KEY_SELECTED_LANGUAGE, languageCode) }

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    private fun getSystemLanguage(): String {
        val systemLocales = LocaleListCompat.getAdjustedDefault()
        return if (!systemLocales.isEmpty) {
            systemLocales[0]?.language ?: "en"
        } else {
            Locale.getDefault().language
        }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\common\TextFieldState.kt ---

package com.pth.androidapp.core.common

data class TextFieldState(
    val text: String = "",
    val error: String? = null
)

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\common\UiState.kt ---

package com.pth.androidapp.core.common

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<out T>(val data: T) : UiState<T>()

    data class Error(val message: String, val code: String? = null) : UiState<Nothing>()

}

inline fun <T, R> UiState<T>.fold(
    onIdle: () -> R,
    onLoading: () -> R,
    onSuccess: (data: T) -> R,
    onError: (message: String, code: String?) -> R
): R {
    return when (this) {
        is UiState.Idle -> onIdle()
        is UiState.Loading -> onLoading()
        is UiState.Success -> onSuccess(this.data)
        is UiState.Error -> onError(this.message, this.code)
    }
}

inline fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> {
    return when (this) {
        is UiState.Idle -> UiState.Idle
        is UiState.Loading -> UiState.Loading
        is UiState.Success -> UiState.Success(transform(data))
        is UiState.Error -> UiState.Error(message, code)
    }
}

inline fun <T> UiState<T>.onSuccess(action: (data: T) -> Unit): UiState<T> {
    if (this is UiState.Success) action(data)
    return this
}

inline fun <T> UiState<T>.onError(action: (message: String, code: String?) -> Unit): UiState<T> {
    if (this is UiState.Error) action(message, code)
    return this
}

inline fun <T> UiState<T>.onLoading(action: () -> Unit): UiState<T> {
    if (this is UiState.Loading) action()
    return this
}

inline fun <T> UiState<T>.onIdle(action: () -> Unit): UiState<T> {
    if (this is UiState.Idle) action()
    return this
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\di\FirebaseModule.kt ---

package com.pth.androidapp.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\di\RepositoryModule.kt ---

package com.pth.androidapp.core.di

import com.pth.androidapp.data.repositories.AuthRepositoryImpl
import com.pth.androidapp.data.repositories.ImageSlideRepositoryImpl
import com.pth.androidapp.domain.repositories.AuthRepository
import com.pth.androidapp.domain.repositories.ImageSlideRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindImageSlideRepository(
        imageSlideRepositoryImpl: ImageSlideRepositoryImpl
    ): ImageSlideRepository
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\extensions\ViewExtensions.kt ---

package com.pth.androidapp.core.extensions

import android.view.View
import androidx.core.view.isVisible

private const val DEFAULT_ANIMATION_DURATION = 300L

fun View.show(duration: Long = DEFAULT_ANIMATION_DURATION) {
    if (!isVisible) {
        isVisible = true
        alpha = 0f
        animate()
            .setDuration(duration)
            .alpha(1f)
            .withStartAction { isVisible = true }
            .start()
    }
}

fun View.hide(duration: Long = DEFAULT_ANIMATION_DURATION) {
    if (isVisible) {
        animate()
            .setDuration(duration)
            .alpha(0f)
            .withEndAction { isVisible = false }
            .start()
    }
}

fun View.toggle(duration: Long = DEFAULT_ANIMATION_DURATION) {
    if (isVisible) hide(duration) else show(duration)
}

fun View.setOnClickListenerWithDebounce(
    debounceTime: Long = 500L,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}

fun View.setEnabledWithFeedback(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1.0f else 0.5f
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\core\utils\ErrorKeys.kt ---

package com.pth.androidapp.core.utils

object ErrorKeys {
    const val USER_NOT_FOUND = "USER_NOT_FOUND"
    const val INVALID_CREDENTIALS = "INVALID_CREDENTIALS"
    const val USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS"
    const val WEAK_PASSWORD = "WEAK_PASSWORD"

    const val SOMETHING_WENT_WRONG = "SOMETHING_WENT_WRONG"

}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\data\local\preferences\UserPreferences.kt ---

package com.pth.androidapp.data.local.preferences

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_UID = "USER_UID"
        const val KEY_USER_EMAIL = "USER_EMAIL"
        const val KEY_LOGIN_EMAIL = "KEY_LOGIN_EMAIL"
        const val KEY_LOGIN_PASSWORD = "KEY_LOGIN_PASSWORD"
        const val KEY_REMEMBER_ME = "KEY_REMEMBER_ME"
    }

    fun saveUserInfo(uid: String, email: String) {
        prefs.edit { putString(KEY_USER_UID, uid); putString(KEY_USER_EMAIL, email) }
    }

    fun clearAll() {
        prefs.edit { clear() }
    }

    fun saveCredentials(email: String, password: String, rememberMe: Boolean) {
        prefs.edit {
            putString(KEY_LOGIN_EMAIL, email)
            putString(KEY_LOGIN_PASSWORD, password)
            putBoolean(KEY_REMEMBER_ME, rememberMe)
        }
    }

    fun clearCredentials() {
        prefs.edit {
            remove(KEY_LOGIN_EMAIL)
            remove(KEY_LOGIN_PASSWORD)
            putBoolean(KEY_REMEMBER_ME, false)
        }
    }

    fun getEmail(): String = prefs.getString(KEY_LOGIN_EMAIL, "") ?: ""
    fun getPassword(): String = prefs.getString(KEY_LOGIN_PASSWORD, "") ?: ""
    fun getRememberMe(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\data\mappers\UserMapper.kt ---

package com.pth.androidapp.data.mappers

import com.google.firebase.auth.FirebaseUser
import com.pth.androidapp.domain.entities.User

fun FirebaseUser.toDomainUser(): User {
    return User(
        id = this.uid,
        email = this.email
    )
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\data\repositories\AuthRepositoryImpl.kt ---

package com.pth.androidapp.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.pth.androidapp.data.local.preferences.UserPreferences
import com.pth.androidapp.data.mappers.toDomainUser
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.core.utils.ErrorKeys
import com.pth.androidapp.domain.repositories.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Exception

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String, rememberMe: Boolean): User {
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception(ErrorKeys.SOMETHING_WENT_WRONG)
            if (rememberMe) {
                userPreferences.saveCredentials(email, password, true)
            } else {
                userPreferences.clearCredentials()
            }

            userPreferences.saveUserInfo(firebaseUser.uid, firebaseUser.email ?: "")

            return firebaseUser.toDomainUser()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthInvalidCredentialsException -> Exception(ErrorKeys.INVALID_CREDENTIALS)
                is FirebaseAuthInvalidUserException -> Exception(ErrorKeys.USER_NOT_FOUND)
                else -> Exception(ErrorKeys.SOMETHING_WENT_WRONG)
            }
        }
    }

    override suspend fun register(email: String, password: String): User {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            val firebaseUser = authResult.user ?: throw Exception(ErrorKeys.SOMETHING_WENT_WRONG)
            logout()
            return firebaseUser.toDomainUser()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthUserCollisionException -> Exception(ErrorKeys.USER_ALREADY_EXISTS)
                is FirebaseAuthWeakPasswordException -> Exception(ErrorKeys.WEAK_PASSWORD)
                is FirebaseAuthInvalidCredentialsException -> Exception(ErrorKeys.INVALID_CREDENTIALS)
                else -> Exception(ErrorKeys.SOMETHING_WENT_WRONG)
            }
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        userPreferences.clearAll()
    }

    override suspend fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\data\repositories\ImageSlideRepositoryImpl.kt ---

package com.pth.androidapp.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.pth.androidapp.domain.models.ImageSlide
import com.pth.androidapp.domain.repositories.ImageSlideRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageSlideRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ImageSlideRepository {

    companion object {
        private const val COLLECTION_IMAGE_SLIDES = "image_slides"
    }

    override suspend fun getAllImageSlides(): List<ImageSlide> {
        return firestore.collection(COLLECTION_IMAGE_SLIDES)
            .get()
            .await()
            .toObjects(ImageSlide::class.java)
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\domain\entities\User.kt ---

package com.pth.androidapp.domain.entities

data class User(
    val id: String,
    val email: String?
)

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\domain\models\ImageSlide.kt ---

package com.pth.androidapp.domain.models

data class ImageSlide(
    val imageUrl: String = "",
    val description: String = ""
)


--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\domain\repositories\AuthRepository.kt ---

package com.pth.androidapp.domain.repositories

import com.pth.androidapp.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, password: String, rememberMe: Boolean): User

    suspend fun register(email: String, password: String): User

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean
}


--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\domain\repositories\ImageSlideRepository.kt ---

package com.pth.androidapp.domain.repositories

import com.pth.androidapp.domain.models.ImageSlide

interface ImageSlideRepository {
    suspend fun getAllImageSlides(): List<ImageSlide>
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\domain\usecases\GetAllImageSlidesUseCase.kt ---

package com.pth.androidapp.domain.usecases

import com.pth.androidapp.domain.models.ImageSlide
import com.pth.androidapp.domain.repositories.ImageSlideRepository
import javax.inject.Inject

class GetAllImageSlidesUseCase @Inject constructor(
    private val repository: ImageSlideRepository
) {
    suspend operator fun invoke(): List<ImageSlide> = repository.getAllImageSlides()
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\domain\usecases\IsUserLoggedInUseCase.kt ---

package com.pth.androidapp.domain.usecases

import com.pth.androidapp.domain.repositories.AuthRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\domain\usecases\LoginUseCase.kt ---

package com.pth.androidapp.domain.usecases

import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): User {
        return authRepository.login(email, password, rememberMe)
    }
}



--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\domain\usecases\RegisterUseCase.kt ---

package com.pth.androidapp.domain.usecases

import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        return authRepository.register(email, password)
    }
}



--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\auth\AuthActivity.kt ---

package com.pth.androidapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pth.androidapp.core.base.activities.BaseActivity
import com.pth.androidapp.core.common.LanguageManager
import com.pth.androidapp.databinding.ActivityAuthBinding
import com.pth.androidapp.domain.repositories.AuthRepository
import com.pth.androidapp.presentation.main.MainActivity
import com.pth.androidapp.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater): ActivityAuthBinding {
        return ActivityAuthBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.isLoggedIn.collectLatest { isLoggedIn ->
                if (isLoggedIn == null) {
                    return@collectLatest
                }

                if (!isLoggedIn) {
                    navigateToMain()
                    finish()
                }
            }
        }
    }

    fun navigateToMain() {
        navigateToActivity(MainActivity::class.java, finishCurrent = true)
    }

}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\auth\AuthViewModel.kt ---

package com.pth.androidapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.domain.usecases.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : BaseViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            _isLoggedIn.value = isUserLoggedInUseCase()
        }
    }
}

--- E:\App\FoodApp\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\auth\fragments\login\LoginFragment.kt ---

package com.pth.androidapp.presentation.auth.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.NotifyType
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.core.utils.ErrorKeys
import com.pth.androidapp.databinding.FragmentLoginBinding
import com.pth.androidapp.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupNavigation()
        observeLoginState()
    }

    private fun setupNavigation() {
        binding.navigateToRegister.setOnClickListener {
            navigateToPage(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect {
                when (it) {
                    is UiState.Loading -> showLoading(true)
                    is UiState.Success -> {
                        showLoading(false)
                        (activity as? AuthActivity)?.navigateToMain()
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        // Handle localization directly here
                        val localizedMessage = when (it.message) {
                            ErrorKeys.USER_NOT_FOUND -> getString(R.string.error_user_not_found)
                            ErrorKeys.INVALID_CREDENTIALS -> getString(R.string.error_invalid_credentials)
                            else -> getString(R.string.some_thing_went_wrong_please_try_again_later)
                        }

                        showNotifyDialog(message = localizedMessage, type = NotifyType.ERROR) { }
                    }
                    is UiState.Idle -> showLoading(false)
                }
            }
        }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\auth\fragments\login\LoginViewModel.kt ---

package com.pth.androidapp.presentation.auth.fragments.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.R
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.InputValidator
import com.pth.androidapp.core.common.TextFieldState
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.data.local.preferences.UserPreferences
import com.pth.androidapp.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPreferences: UserPreferences,
    private val validator: InputValidator,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _loginState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    val emailState = MutableStateFlow(TextFieldState(""))
    val passwordState = MutableStateFlow(TextFieldState(""))
    val rememberMeState = MutableStateFlow(false)

    init {
        loadSavedCredentials()
    }

    private fun loadSavedCredentials() {
        viewModelScope.launch {
            if (userPreferences.getRememberMe()) {
                emailState.value = TextFieldState(userPreferences.getEmail())
                passwordState.value = TextFieldState(userPreferences.getPassword())
                rememberMeState.value = true
            }
        }
    }

    fun onLoginClicked() {
        if (!validateForm()) return
        execute(_loginState) {
            loginUseCase(
                email = emailState.value.text.trim(),
                password = passwordState.value.text,
                rememberMe = rememberMeState.value
            )
        }
    }

    private fun validateForm(): Boolean {
        val email = emailState.value.text
        val password = passwordState.value.text

        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)

        return isEmailValid && isPasswordValid
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            email.isBlank() -> {
                emailState.update { it.copy(error = context.getString(R.string.email_not_empty)) }
                false
            }
            !validator.validateEmail(email) -> {
                emailState.update { it.copy(error = context.getString(R.string.email_not_valid)) }
                false
            }
            else -> {
                emailState.update { it.copy(error = null) }
                true
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return when {
            password.isBlank() -> {
                passwordState.update { it.copy(error = context.getString(R.string.password_not_empty)) }
                false
            }
            !validator.validatePassword(password) -> {
                passwordState.update { it.copy(error = context.getString(R.string.password_not_valid)) }
                false
            }
            else -> {
                passwordState.update { it.copy(error = null) }
                true
            }
        }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\auth\fragments\register\RegisterFragment.kt ---

package com.pth.androidapp.presentation.auth.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.NotifyType
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.core.utils.ErrorKeys
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupNavigation()
        observeRegisterState()
    }

    private fun setupNavigation() {
        binding.navigateToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun observeRegisterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerState.collect {
                when (it) {
                    is UiState.Loading -> showLoading(true)
                    is UiState.Success -> {
                        showLoading(false)
                        showNotifyDialog(
                            type = NotifyType.SUCCESS,
                            message = getString(R.string.registration_success)
                        ) {
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        // Handle localization directly here
                        val localizedMessage = when (it.message) {
                            ErrorKeys.INVALID_CREDENTIALS -> getString(R.string.error_invalid_credentials)
                            ErrorKeys.USER_ALREADY_EXISTS -> getString(R.string.error_user_already_exists)
                            ErrorKeys.WEAK_PASSWORD -> getString(R.string.error_weak_password)
                            else -> getString(R.string.some_thing_went_wrong_please_try_again_later)
                        }

                        showNotifyDialog(message = localizedMessage, type = NotifyType.ERROR) { }
                    }
                    is UiState.Idle -> showLoading(false)
                }
            }
        }
    }

}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\auth\fragments\register\RegisterViewModel.kt ---

package com.pth.androidapp.presentation.auth.fragments.register

import android.content.Context
import com.pth.androidapp.R
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.InputValidator
import com.pth.androidapp.core.common.TextFieldState
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val validator: InputValidator,
    @ApplicationContext private val context: Context
) : BaseViewModel() {
    private val _registerState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val registerState = _registerState.asStateFlow()

    val emailState = MutableStateFlow(TextFieldState(""))
    val passwordState = MutableStateFlow(TextFieldState(""))
    val confirmPasswordState = MutableStateFlow(TextFieldState(""))

    fun onRegisterClicked() {
        if (!validateForm()) return

        execute(_registerState) {
            registerUseCase(
                email = emailState.value.text.trim(),
                password = passwordState.value.text
            )
        }
    }

    private fun validateForm(): Boolean {
        val email = emailState.value.text
        val password = passwordState.value.text
        val confirmPassword = confirmPasswordState.value.text

        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)
        val isConfirmPasswordValid = validateConfirmPassword(password, confirmPassword)

        return isEmailValid && isPasswordValid && isConfirmPasswordValid
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            email.isBlank() -> {
                emailState.update { it.copy(error = context.getString(R.string.email_not_empty)) }
                false
            }
            !validator.validateEmail(email) -> {
                emailState.update { it.copy(error = context.getString(R.string.email_not_valid)) }
                false
            }
            else -> {
                emailState.update { it.copy(error = null) }
                true
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return when {
            password.isBlank() -> {
                passwordState.update { it.copy(error = context.getString(R.string.password_not_empty)) }
                false
            }
            !validator.validatePassword(password) -> {
                passwordState.update { it.copy(error = context.getString(R.string.password_not_valid)) }
                false
            }
            else -> {
                passwordState.update { it.copy(error = null) }
                true
            }
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        return when {
            confirmPassword.isBlank() -> {
                confirmPasswordState.update { it.copy(error = context.getString(R.string.password_not_empty)) }
                false
            }
            !validator.validatePassword(confirmPassword) -> {
                confirmPasswordState.update { it.copy(error = context.getString(R.string.password_not_valid)) }
                false
            }
            password != confirmPassword -> {
                confirmPasswordState.update { it.copy(error = context.getString(R.string.password_not_match)) }
                false
            }
            else -> {
                confirmPasswordState.update { it.copy(error = null) }
                true
            }
        }
    }

}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\MainActivity.kt ---

package com.pth.androidapp.presentation.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pth.androidapp.R
import com.pth.androidapp.core.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivityMainBinding
import com.pth.androidapp.presentation.auth.AuthActivity
import com.pth.androidapp.presentation.main.navigation.MainTabNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var tabNavigator: MainTabNavigator


    override fun inflateBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            viewModel.isLoggedIn.collectLatest { isLoggedIn ->
                if (isLoggedIn == null) {
                    return@collectLatest
                }

                if (isLoggedIn) {
                    navigateToAuth()
                    finish()
                }
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        tabNavigator = MainTabNavigator(this, window, binding, navController)
        tabNavigator.setupTabs()
    }

    fun navigateToAuth() {
        navigateToActivity(AuthActivity::class.java, finishCurrent = true)
    }

}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\MainViewModel.kt ---

package com.pth.androidapp.presentation.main

import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.domain.usecases.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : BaseViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {

        viewModelScope.launch {
            _isLoggedIn.value = isUserLoggedInUseCase()
        }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\fragments\dineout\DineoutFragment.kt ---

package com.pth.androidapp.presentation.main.fragments.dineout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pth.androidapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DineoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DineoutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dineout, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DineoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = 
            DineoutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\fragments\food\FoodFragment.kt ---

package com.pth.androidapp.presentation.main.fragments.food

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pth.androidapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FoodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoodFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FoodFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = 
            FoodFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\fragments\genie\GenieFragment.kt ---

package com.pth.androidapp.presentation.main.fragments.genie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pth.androidapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenieFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genie, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GenieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = 
            GenieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\fragments\home\HomeFragment.kt ---

package com.pth.androidapp.presentation.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeImageSlides()
    }

    private fun observeImageSlides() {

    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\fragments\home\HomeViewModel.kt ---

package com.pth.androidapp.presentation.main.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.domain.models.ImageSlide
import com.pth.androidapp.domain.usecases.GetAllImageSlidesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllImageSlidesUseCase: GetAllImageSlidesUseCase
) : BaseViewModel() {

    private val _imageSlides = MutableStateFlow<UiState<List<ImageSlide>>>(UiState.Idle)
    val imageSlides: StateFlow<UiState<List<ImageSlide>>> = _imageSlides.asStateFlow()

    init {
        fetchImageSlides()
    }

    private fun fetchImageSlides() {
        execute(_imageSlides) {
            getAllImageSlidesUseCase()
        }

    }
}


--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\fragments\instamart\InstamartFragment.kt ---

package com.pth.androidapp.presentation.main.fragments.instamart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pth.androidapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InstamartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InstamartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instamart, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InstamartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = 
            InstamartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\navigation\MainTabNavigator.kt ---

package com.pth.androidapp.presentation.main.navigation

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.pth.androidapp.R
import com.pth.androidapp.databinding.ActivityMainBinding

/**
 * Handles tab navigation and UI updates for MainActivity.
 */
@RequiresApi(Build.VERSION_CODES.R)
class MainTabNavigator(
    private val context: Context,
    private val window: Window,
    private val binding: ActivityMainBinding,
    private val navController: NavController
) {
    private val tabs = mutableListOf<Tab>()
    private val fragmentToTab = mutableMapOf<Int, Tab>()

    fun setupTabs() {
        addTab(
            binding.navHomeContainer,
            binding.navHomeIndicator,
            binding.ivNavHome,
            binding.tvNavHome,
            R.id.homeFragment,
            true,
            R.color.white,
            true
        )
        addTab(
            binding.navFoodContainer,
            binding.navFoodIndicator,
            binding.ivNavFood,
            binding.tvNavFood,
            R.id.foodFragment,
            false,
            R.color.white,
            true
        )
        addTab(
            binding.navInstamartContainer,
            binding.navInstamartIndicator,
            binding.ivNavInstamart,
            binding.tvNavInstamart,
            R.id.instamartFragment,
            false,
            R.color.bg_pink,
            true
        )
        addTab(
            binding.navDineoutContainer,
            binding.navDineoutIndicator,
            binding.ivNavDineout,
            binding.tvNavDineout,
            R.id.dineoutFragment,
            false,
            R.color.black,
            false
        )
        addTab(
            binding.navGenieContainer,
            binding.navGenieIndicator,
            binding.ivNavGenie,
            binding.tvNavGenie,
            R.id.genieFragment,
            false,
            R.color.bg_violet,
            true
        )
        setupNavigation()
    }

    private fun addTab(
        container: View,
        indicator: View,
        icon: android.widget.ImageView,
        text: android.widget.TextView,
        fragmentId: Int,
        alwaysOrangeIcon: Boolean,
        @ColorRes statusBarColorRes: Int,
        isLightStatusBar: Boolean
    ) {
        val tab = Tab(
            container,
            indicator,
            icon,
            text,
            fragmentId,
            alwaysOrangeIcon,
            statusBarColorRes,
            isLightStatusBar
        )
        tabs.add(tab)
        fragmentToTab[fragmentId] = tab
    }

    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateUIForDestination(destination.id)
        }
        tabs.forEach { tab ->
            tab.container.setOnClickListener { navController.navigate(tab.fragmentId) }
        }
    }

    private fun updateUIForDestination(destinationId: Int) {
        resetAllViews()
        fragmentToTab[destinationId]?.let { updateTabSelection(it) }
        updateStatusBar(destinationId)
    }

    private fun resetAllViews() {
        tabs.forEach { tab ->
            tab.indicator.visibility = View.GONE
            updateTabStyle(tab, false)
        }
    }

    private fun updateTabSelection(tab: Tab) {
        tab.indicator.visibility = View.VISIBLE
        updateTabStyle(tab, true)
    }

    private fun updateTabStyle(tab: Tab, isSelected: Boolean) {
        val textColor = ContextCompat.getColor(context, if (isSelected) R.color.orange else R.color.dark_grey)
        val iconColor = ContextCompat.getColor(
            context,
            if (isSelected || tab.alwaysOrangeIcon) R.color.orange else R.color.dark_grey
        )
        tab.text.setTextColor(textColor)
        tab.text.typeface = Typeface.create(tab.text.typeface, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        tab.icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
    }

    private fun updateStatusBar(destinationId: Int) {
        fragmentToTab[destinationId]?.let { tab ->
            window.statusBarColor = ContextCompat.getColor(context, tab.statusBarColorRes)
            window.decorView.windowInsetsController?.let { controller ->
                val appearance = if (tab.isLightStatusBar) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0
                controller.setSystemBarsAppearance(appearance, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            }
        }
    }
}


--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\main\navigation\Tab.kt ---

package com.pth.androidapp.presentation.main.navigation

import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Data class representing a navigation tab in MainActivity.
 */
data class Tab(
    val container: View,
    val indicator: View,
    val icon: ImageView,
    val text: TextView,
    val fragmentId: Int,
    val alwaysOrangeIcon: Boolean = false,
    val statusBarColorRes: Int,
    val isLightStatusBar: Boolean
)



--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\splash\SplashActivity.kt ---

package com.pth.androidapp.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.pth.androidapp.R
import com.pth.androidapp.core.base.activities.BaseActivity
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.databinding.ActivitySplashBinding
import com.pth.androidapp.presentation.auth.AuthActivity
import com.pth.androidapp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()
    private var isVideoCompleted = false

    override fun inflateBinding(inflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playSplashScreenVideo()
    }

    private fun playSplashScreenVideo() {
        val videoUri = "android.resource://${packageName}/${R.raw.splashscreen}".toUri()
        binding.videoView.apply {
            setVideoURI(videoUri)
            setOnPreparedListener { it.start() }
            setOnCompletionListener {
                isVideoCompleted = true
                proceedToNextScreen()
            }
        }
    }

    private fun proceedToNextScreen() {
        if (isVideoCompleted) {
            lifecycleScope.launch {
                viewModel.isLoggedIn.collectLatest { isLoggedIn ->
                    if (isLoggedIn == null) {
                        return@collectLatest
                    }

                    if (isLoggedIn) {
                        navigateToMain()
                    } else {
                        navigateToAuth()
                    }
                    finish()
                }
            }
        }
    }

    private fun navigateToMain() {
        navigateToActivity(MainActivity::class.java, finishCurrent = true)
    }

    private fun navigateToAuth() {
        overridePendingTransition(R.anim.slide_up, 0)
        navigateToActivity(AuthActivity::class.java, finishCurrent = true)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.stopPlayback()
    }
}

--- E:\App\FoodApp_Android\app\src\main\java\com\pth\androidapp\presentation\splash\SplashViewModel.kt ---

package com.pth.androidapp.presentation.splash

import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.domain.usecases.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : BaseViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            _isLoggedIn.value = isUserLoggedInUseCase()
        }
    }
}

--- E:\App\FoodApp_Android\app\src\test\java\com\pth\androidapp\ExampleUnitTest.kt ---

package com.pth.androidapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}


--- End of content ---
