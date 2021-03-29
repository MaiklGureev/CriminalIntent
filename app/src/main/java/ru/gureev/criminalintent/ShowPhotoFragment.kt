package ru.gureev.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment


private const val ARG_PARAM_CRIME = "crime"


class ShowPhotoFragment : DialogFragment() {
    private lateinit var imageView: ImageView
    private var crimePhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            crimePhotoPath = it.getSerializable(ARG_PARAM_CRIME) as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_photo, container, false)
        imageView = view.findViewById(R.id.imageView) as ImageView
        imageView.setImageBitmap(crimePhotoPath?.let {
            PictureUtils.getScaledBitmap(
                it,
                requireActivity()
            )
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(crimePhotoPath: String?) =
            ShowPhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_CRIME, crimePhotoPath)
                }
            }
    }
}