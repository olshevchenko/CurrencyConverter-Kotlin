package com.olshevchenko.currencyconverter.features.converter.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.olshevchenko.currencyconverter.databinding.FragmentConverterBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConverterFragment : Fragment(), ConverterContracts.View {

    companion object {
        fun newInstance() = ConverterFragment()
    }

    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!

    // Lazy Inject ViewModel
    private val _viewModel by viewModel<ConverterViewModel>()

    // Lazy Inject presenters
    private val _codesPresenter: CodesPresenter by inject()

    //    private val _codesPresenter: CodesPresenter by inject {
//        parametersOf(getCurrencyCodesUseCase???, _viewModel) - ToDo need passing values to inject ???
//    }
    private val _ratePresenter: RatePresenter by inject()
    private val _refreshPresenter: RefreshPresenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            converterViewModel = _viewModel
            codesPresenter = _codesPresenter
            ratePresenter = _ratePresenter
            refreshPresenter = _refreshPresenter
        }

        setLiveDataObservers()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.refreshPresenter?.dispose() // clear disposables now
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.refreshPresenter?.init() // promptly try to refresh currency rates
    }

    override fun initView() {
//        TODO("Not yet implemented")
    }

    private fun setLiveDataObservers() {
        binding.converterViewModel?.let {
//            it.isRefreshingState.observe(
//                viewLifecycleOwner, Observer(::onRefreshingStateReceived)
//            )
            it.isCodesLoadingState.observe(
                viewLifecycleOwner, Observer(::onCodesLoadingStateReceived)
            )
            it.gotSomeError.observe(
                viewLifecycleOwner, Observer(::onSomeErrorReceived)
            )
        }
    }

//    override fun onRefreshingStateReceived(isRefreshing: Boolean) {
//        showRefreshingProgress(isRefreshing)
//    }

    override fun onCodesLoadingStateReceived(isLoading: Boolean) {
//        showRefreshingProgress(isRefreshing)
    }

    override fun onSomeErrorReceived(errMsg: String?) {
        errMsg?.let {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()

//            val snackBar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
//            snackBar.setAction(actionText) { _ -> action.invoke() }
//            snackBar.setActionTextColor(ContextCompat.getColor(appContext, color.colorTextPrimary))
//            snackBar.show()
        }
    }


}