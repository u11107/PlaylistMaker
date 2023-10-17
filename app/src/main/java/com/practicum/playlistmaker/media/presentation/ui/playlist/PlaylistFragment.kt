package com.practicum.playlistmaker.media.presentation.ui.playlist

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.models.PlaylistScreenResult
import com.practicum.playlistmaker.media.presentation.models.PlaylistScreenState
import com.practicum.playlistmaker.media.presentation.view_model.PlaylistViewModel
import com.practicum.playlistmaker.player.presentation.ui.PlayerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

open class PlaylistFragment: Fragment() {
    protected lateinit var binding: FragmentPlaylistBinding
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    protected open val viewModel: PlaylistViewModel by viewModel()

    private val backPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.needShowDialog()) {
                confirmDialog.show()
            } else {
                viewModel.onCancelPlaylist()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.onPlaylistImageChanged(uri)
                    setPlaylistImage(uri)
                }
            }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getAddPlaylistTrigger().observe(viewLifecycleOwner) {
            addPlaylist(it)
        }

        viewModel.getResult().observe(viewLifecycleOwner) {
            renderResult(it)
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.create_playlist_dialog_title))
            .setMessage(R.string.create_playlist_dialog_message)
            .setNegativeButton(R.string.create_playlist_dialog_cancel_button) {_, _ -> }
            .setPositiveButton(R.string.create_playlist_dialog_yes_button) { _, _ ->
                viewModel.onCancelPlaylist()
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)

        binding.btPlaylistBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.edPlaylistName.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.onPlaylistNameChanged(text.toString())
        }

        binding.edPlaylistDescription.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.onPlaylistDescriptionChanged(text.toString())
        }

        binding.playlistImageView.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btCreatePlaylist.setOnClickListener {
            viewModel.onAddPlaylistClick()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(PLAYLIST_NAME, binding.edPlaylistName.editText?.text.toString())
        outState.putString(PLAYLIST_DESCRIPTION, binding.edPlaylistDescription.editText?.text.toString())
        if (binding.playlistImageView.tag != null) {
            outState.putString(PLAYLIST_IMAGE, (binding.playlistImageView.tag as Uri).toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            binding.edPlaylistName.editText?.setText(savedInstanceState.getString(PLAYLIST_NAME))
            binding.edPlaylistDescription.editText?.setText(savedInstanceState.getString(
                PLAYLIST_DESCRIPTION
            ))
            val stringUri = savedInstanceState.getString(PLAYLIST_IMAGE)
            if (!stringUri.isNullOrEmpty()) {
                setPlaylistImage(Uri.parse(stringUri))
            }
        }
    }

    private fun render(state: PlaylistScreenState) {
        binding.btCreatePlaylist.isEnabled = state is PlaylistScreenState.Filled
        setEditTextStyle(binding.edPlaylistName, state is PlaylistScreenState.Filled)

        val content: Playlist? = when (state) {
            is PlaylistScreenState.Filled -> state.content
            is PlaylistScreenState.NotEmpty -> state.content
            else -> null
        }
        setEditTextStyle(binding.edPlaylistDescription, !content?.description.isNullOrEmpty())
    }

    protected open fun renderResult(result: PlaylistScreenResult) {
        when (result) {
            is PlaylistScreenResult.Canceled -> {
                setFragmentResult(RESULT_KEY, bundleOf(KEY_PLAYLIST_ID to 0, KEY_PLAYLIST_NAME to ""))
                privateNavigateUp()
            }
            is PlaylistScreenResult.Created -> {
                setFragmentResult(RESULT_KEY, bundleOf(KEY_PLAYLIST_ID to result.content.id, KEY_PLAYLIST_NAME to result.content.name))
                Toast.makeText(requireContext(), "Плейлист " + result.content.name + " создан", Toast.LENGTH_LONG).show()
                privateNavigateUp()
            }
        }
    }

    protected fun privateNavigateUp() {
        hideKeyboard()
        backPressedCallback.isEnabled = false
        if (!tag.equals(PlayerActivity.FRAGMENT_TAG)) {
            findNavController().navigateUp()
        } else {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setEditTextStyle(textInputLayout: TextInputLayout, filled: Boolean) {
        val coloInt =  if (filled) R.color.edittext_playlist_selector_text else R.color.edittext_playlist_selector
        val colorStateList = ResourcesCompat.getColorStateList(resources, coloInt, requireContext().theme)
        textInputLayout.setBoxStrokeColorStateList(colorStateList!!)
        textInputLayout.defaultHintTextColor = colorStateList
        textInputLayout.hintTextColor = colorStateList
    }

    private fun setPlaylistImage(uri: Uri?) {
        if (uri != null) {
            binding.playlistImageView.setImageURI(uri)
            binding.playlistImageView.clipToOutline = true
            binding.playlistImageView.tag = uri
        }
    }

    private fun addPlaylist(playlist: Playlist) {
        if (binding.playlistImageView.tag != null && !playlist.filePath.isNullOrEmpty()) {
            val uri : Uri = binding.playlistImageView.tag as Uri
            viewModel.saveImageToPrivateStorage(requireActivity().contentResolver.openInputStream(uri))
        }
        viewModel.addPlaylist(playlist)
    }

    private fun hideKeyboard() {
        val imm =requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.edPlaylistName.windowToken, 0)
    }

    companion object {
        const val IMAGE_DIR = "playlistImage"
        const val RESULT_KEY = "playlist"
        const val KEY_PLAYLIST_ID = "id"
        const val KEY_PLAYLIST_NAME = "name"

        private const val PLAYLIST_IMAGE = "PLAYLIST_IMAGE"
        private const val PLAYLIST_NAME = "PLAYLIST_NAME"
        private const val PLAYLIST_DESCRIPTION = "PLAYLIST_DESCRIPTION"
    }
}