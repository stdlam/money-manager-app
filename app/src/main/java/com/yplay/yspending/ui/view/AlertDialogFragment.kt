package com.yplay.yspending.ui.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.yplay.yspending.R

class AlertDialogFragment: DialogFragment(), DialogInterface.OnKeyListener {

    companion object {
        private val TAG = AlertDialogFragment::class.simpleName
        private const val ARGUMENT_TAG = "ARGUMENT_TAG"
        private const val ARGUMENT_BUNDLE = "ARGUMENT_BUNDLE"
        private const val ARGUMENT_LAYOUT_ID = "ARGUMENT_LAYOUT_ID"
        private const val ARGUMENT_TEXT_VIEWS = "ARGUMENT_TEXT_VIEWS"
        private const val ARGUMENT_DISMISS_VIEWS = "ARGUMENT_DISMISS_VIEWS"
        private const val ARGUMENT_ICON_VIEWS = "ARGUMENT_ICON_VIEWS"
        private const val ARGUMENT_EDIT_TEXT_VIEWS = "ARGUMENT_EDIT_TEXT_VIEWS"

        const val EXTRA_SPENDING = "EXTRA_SPENDING"
        const val EXTRA_PRICE = "EXTRA_PRICE"

        fun newInstance(tag: String, bundle: Bundle?, layoutId: Int, textView: HashMap<Int, Pair<String, Int>>, dismissView: ArrayList<Int>,
                        iconViews: HashMap<Int, Int>, editTextViews: HashMap<Int, String>): AlertDialogFragment {
            val fragment = AlertDialogFragment()
            val arguments = Bundle()
            arguments.putString(ARGUMENT_TAG, tag)
            arguments.putBundle(ARGUMENT_BUNDLE, bundle)
            arguments.putInt(ARGUMENT_LAYOUT_ID, layoutId)
            arguments.putSerializable(ARGUMENT_TEXT_VIEWS, textView)
            arguments.putSerializable(ARGUMENT_DISMISS_VIEWS, dismissView)
            arguments.putSerializable(ARGUMENT_ICON_VIEWS, iconViews)
            arguments.putSerializable(ARGUMENT_EDIT_TEXT_VIEWS, editTextViews)
            fragment.arguments = arguments
            return fragment
        }
    }

    private var mIsDismissCalled = false
    private var mLayoutId = 0
    private lateinit var mTextView: HashMap<Int, Pair<String, Int>>
    private lateinit var mDismissViews: ArrayList<Int>
    private lateinit var mIconViews: HashMap<Int, Int>
    private lateinit var mEditTextViews: HashMap<Int, String>
    private lateinit var mData: Intent
    private var mTag = ""
    private var mListener: OnDialogFragmentResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        mData = Intent()
        mTextView = HashMap()
        mDismissViews = ArrayList()
        mEditTextViews = HashMap()

        arguments?.let {
            mTag = it.getString(ARGUMENT_TAG) ?: ""
            val bundle = it.getBundle(ARGUMENT_BUNDLE)
            if (bundle != null) {
                mData.putExtras(bundle)
            }
            mLayoutId = it.getInt(ARGUMENT_LAYOUT_ID)
            mTextView = it.getSerializable(ARGUMENT_TEXT_VIEWS) as HashMap<Int, Pair<String, Int>>
            mDismissViews = it.getSerializable(ARGUMENT_DISMISS_VIEWS) as ArrayList<Int>
            mIconViews = it.getSerializable(ARGUMENT_ICON_VIEWS) as HashMap<Int, Int>
            mEditTextViews = it.getSerializable(ARGUMENT_EDIT_TEXT_VIEWS) as HashMap<Int, String>
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(mLayoutId, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        if (mTextView.isNotEmpty()) {
            for ((key, value) in mTextView) {
                val v = view.findViewById<View>(key)
                if (v == null) {
                    Log.e(TAG, "text view is null on mTag = $mTag")
                } else {
                    (v as TextView).text = value.first
                    v.visibility = value.second
                }
            }
        }

        if (mDismissViews.isNotEmpty()) {
            for (viewID in mDismissViews) {
                if (view.findViewById<View>(viewID) == null) {
                    Log.e(TAG, "dismiss view is null on mTag = $mTag")
                } else {
                    view.findViewById<View>(viewID).setOnClickListener {
                        mEditTextViews.forEach { (viewID, _) ->
                            val et = view.findViewById<View>(viewID)
                            if (viewID == R.id.et_spending && et is EditText) {
                                mData.putExtra(EXTRA_SPENDING, et.text.toString())
                            } else if (viewID == R.id.et_price && et is EditText) {
                                mData.putExtra(EXTRA_PRICE, et.text.toString())
                            }
                        }
                        mListener?.onDialogResult(mTag, viewID, mData)
                        dismiss()
                    }
                }
            }
        }

        mIconViews.forEach { (viewId, drawableSrc) ->
            val ic = view.findViewById<View>(viewId)
            ic?.let {
                if (it is ImageView) {
                    val drawable = it.context.getDrawable(drawableSrc)
                    it.setImageDrawable(drawable)
                    it.visibility = View.VISIBLE
                }
            }
        }

        mEditTextViews.forEach { (viewID, text) ->
            val et = view.findViewById<View>(viewID)
            et?.let {
                if (it is EditText && !text.isBlank()) {
                    it.setOnFocusChangeListener { _, hasFocus ->
                        Log.e(TAG, "${it.text.toString()} hasFocus = $hasFocus")
                    }
                    it.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            Log.e(TAG, "afterTextChanged = ${s.toString()}")
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                            Log.e(TAG, "beforeTextChanged = ${s.toString()}")
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            Log.e(TAG, "onTextChanged = ${s.toString()}")
                        }
                    })
                    it.setText(text)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        parentFragment?.let {
            if (it is OnDialogFragmentResult) {
                mListener = it
            }
        }
        if (mListener == null && context is OnDialogFragmentResult) {
            mListener = context
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        Log.d(TAG, "setupDialog")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        window?.let {
            it.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            it.setBackgroundDrawableResource(R.color.transparent)
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE)
            dialog.setOnKeyListener(this)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        Log.d(TAG, "show")
        val ft = manager.beginTransaction()
        val prev = manager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
            .add(this, tag)
            .commitAllowingStateLoss()
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        return true
    }

    override fun dismiss() {
        Log.d(TAG, "dismiss")
        mIsDismissCalled = true
        fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
    }

    class Builder(private val mLayoutId: Int) {
        private val mTextViews = HashMap<Int, Pair<String, Int>>()
        private val mDismissViews = ArrayList<Int>()
        private var mIconViews = HashMap<Int, Int>()
        private var mEditTextViews = HashMap<Int, String>()

        @NonNull
        fun create(tag: String): AlertDialogFragment {
            return create(tag, null)
        }

        @NonNull
        fun create(tag: String, arguments: Bundle?): AlertDialogFragment {
            return newInstance(tag, arguments, mLayoutId, mTextViews, mDismissViews, mIconViews, mEditTextViews)
        }

        fun addTextView(@IdRes viewId: Int, text: String, visible: Int = View.VISIBLE): Builder {
            mTextViews[viewId] = Pair(text, visible)
            return this
        }

        fun addDismissView(@IdRes viewId: Int): Builder {
            mDismissViews.add(viewId)
            return this
        }

        fun addIconView(@IdRes viewId: Int, @DrawableRes drawableRes: Int): Builder {
            mIconViews[viewId] = drawableRes
            return this
        }

        fun addEditTextView(@IdRes viewId: Int, text: String = ""): Builder {
            mEditTextViews[viewId] = text
            return this
        }

        fun show(manager: FragmentManager, tag: String): AlertDialogFragment {
            val dialogFragment = create(tag)
            dialogFragment.setStyle(STYLE_NO_TITLE, R.style.AppTheme)
            dialogFragment.show(manager, tag)
            return dialogFragment
        }

    }

    interface OnDialogFragmentResult {
        fun onDialogResult(tag: String, actionId: Int, data: Intent)
    }

}