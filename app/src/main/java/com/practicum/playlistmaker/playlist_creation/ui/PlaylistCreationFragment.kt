package com.practicum.playlistmaker.playlist_creation.ui

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.practicum.playlistmaker.playlist_creation.view_model.CreateButtonState
import com.practicum.playlistmaker.playlist_creation.view_model.PermissionState
import com.practicum.playlistmaker.playlist_creation.view_model.PlaylistCreationState
import com.practicum.playlistmaker.playlist_creation.view_model.PlaylistCreationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
open class PlaylistCreationFragment : Fragment() {

    protected open val viewModel: PlaylistCreationViewModel by viewModel()
    protected open lateinit var binding: FragmentPlaylistCreationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(binding.plCover)
                        .load(uri.toString())
                        .centerCrop()
                        .into(binding.plCover)
                    viewModel.saveCoverUri(uri)
                }
            }

        viewModel.observePermissionState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PermissionState.GRANTED -> pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )

                PermissionState.DENIED_PERMANENTLY -> displaySettings()
                else -> permissionRationaleToast()
            }
        }

        binding.plCover.setOnClickListener {
            viewModel.onCoverClicked()
        }

        binding.playlistName.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text)
            setHintAndBoxColor(text, binding.playlistNameLayout)
        }

        binding.playlistDescription.doOnTextChanged { text, _, _, _ ->
            viewModel.onDescriptionChanged(text)
            setHintAndBoxColor(text, binding.playlistDescriptionLayout)
        }

        binding.createPlaylist.setOnClickListener {
            viewModel.onCreatePlClicked()
            playlistCreatedSnackbar(binding.root)
        }

        val confirmDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.AppTheme_MyMaterialAlertDialog)
                .setTitle(resources.getString(R.string.save_pl_dialog_title))
                .setMessage(resources.getString(R.string.save_pl_dialog_message))
                .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.finish)) { _, _ -> findNavController().navigateUp() }

        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBackPressed()
        }

        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PlaylistCreationState.EMPTY_STATE -> findNavController().navigateUp()
                PlaylistCreationState.PLAYLIST_CREATED -> findNavController().navigateUp()
                PlaylistCreationState.SHOW_DIALOG -> {
                    confirmDialog.show()
                }
            }
        }

        viewModel.observeCreateButtonState().observe(viewLifecycleOwner) { state ->
            when (state) {
                CreateButtonState.DISABLED -> binding.createPlaylist.isEnabled = false
                CreateButtonState.ENABLED -> binding.createPlaylist.isEnabled = true
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.onBackPressed()
        }
    }

    protected open fun playlistCreatedSnackbar(view: View) {
        val snackbar = Snackbar.make(
            requireContext(),
            view,
            "${getString(R.string.playlist)} ${binding.playlistName.text} ${getString(R.string.created)}",
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.black_light_white_night
            )
        )
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            textSize = resources.getDimension(R.dimen.snackbar_text_size)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            typeface = Typeface.DEFAULT
        }
        snackbar.show()
    }

    private fun displaySettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.fromParts("package", requireContext().packageName, null)
        requireContext().startActivity(intent)
    }

    private fun permissionRationaleToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.read_image_rationale),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setHintAndBoxColor(text: CharSequence?, view: TextInputLayout) {
        if (text.isNullOrEmpty()) {
            view.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.pl_creation_edit_text)
            ContextCompat.getColorStateList(requireContext(), R.color.pl_creation_edit_text)
                ?.let { view.setBoxStrokeColorStateList(it) }
        } else {
            view.defaultHintTextColor =
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.pl_creation_edit_text_blue
                )
            ContextCompat.getColorStateList(requireContext(), R.color.pl_creation_edit_text_blue)
                ?.let { view.setBoxStrokeColorStateList(it) }
        }
    }

}