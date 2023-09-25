package  com.example.playlistmaker.media.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.example.playlistmaker.media.ui.model.Playlist
import com.example.playlistmaker.media.ui.viewModel.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlayListBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    private val viewModel by viewModel<NewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPickMediaRegister()
        initListeners()
    }

    private fun initListeners() {

        binding.buttonCreatePlaylist.setOnClickListener {
            val playlist = Playlist(
                title = binding.editTextPlaylistTitle.text.toString(),
                description = binding.editTextPlaylistDescription.text.toString(),
                imageUri = imageUri,
                trackList = "",
                size = 0
            )

            viewModel.savePlaylist(playlist)

            imageUri?.let { viewModel.saveToLocalStorage(uri = it) }

            Toast.makeText(
                requireContext(),
                String.format(getString(R.string.playlist_created), playlist.title),
                Toast.LENGTH_SHORT
            ).show()

            findNavController().popBackStack()
        }

        binding.newPlaylistToolbar.setOnClickListener {
            if (binding.editTextPlaylistTitle.text.toString().isNotEmpty() ||
                binding.editTextPlaylistDescription.text.toString()
                    .isNotEmpty() || imageUri != null
            )
                showConfirmDialog()
            else {
                findNavController().navigateUp()
            }
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (imageUri != null ||
                    !binding.editTextPlaylistTitle.text.isNullOrEmpty() ||
                    !binding.editTextPlaylistDescription.text.isNullOrEmpty()) {
                    showConfirmDialog()
                } else {
                    findNavController().navigateUp()
                }
            }
        })

        binding.editTextPlaylistTitle.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.buttonCreatePlaylist.isEnabled = true
                    context?.let {
                        ContextCompat.getColor(
                            it,
                            R.color.switcher
                        )
                    }?.let {
                        binding.buttonCreatePlaylist.setBackgroundColor(
                            it
                        )
                    }
                } else {
                    binding.buttonCreatePlaylist.isEnabled = false
                    context?.let {
                        ContextCompat.getColor(
                            it,
                            R.color.main_grey_color
                        )
                    }?.let {
                        binding.buttonCreatePlaylist.setBackgroundColor(
                            it
                        )
                    }
                }
            }
        }
    }

    private fun initPickMediaRegister() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.pickImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.pickImage.setImageURI(uri)
                    imageUri = uri
                }
            }

        binding.pickImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun showConfirmDialog() {
        context?.let { context ->
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.stop_creating_playlist)
                .setMessage(R.string.unsaved_data_will_be_lost)
                .setNeutralButton(R.string.cancel) { dialog, which ->
                }
                .setPositiveButton(R.string.finish) { dialog, which ->
                    findNavController().popBackStack()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}