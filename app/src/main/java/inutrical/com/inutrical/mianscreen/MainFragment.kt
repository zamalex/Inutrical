package inutrical.com.inutrical.mianscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import inutrical.com.inutrical.R
import inutrical.com.inutrical.api.LocalData
import inutrical.com.inutrical.calculate.CalculateFragment
import inutrical.com.inutrical.main.MainActivity
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A simple [Fragment] subclass.
*/
class MainFragment : Fragment() ,onMainClick{

    val ars:MainFragmentArgs by navArgs()
    lateinit var mview: View

    lateinit var mAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_main, container, false)

        mAdapter =
            MainAdapter(requireActivity(), this)

        mview.main_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }


        (requireActivity() as MainActivity).setData(LocalData.getUser(requireActivity()).name!!,LocalData.getUser(requireActivity()).mail!!)
        return mview
    }

    override fun onClickMain(pos: Int) {
        if (pos==0){

                mview.findNavController().navigate(R.id.action_mainFragment_to_calculateFragment)

        }
        else if (pos==1){
            mview.findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).changeToolbar(true)
        (activity as MainActivity).showToggle(true)
        (activity as MainActivity).supportActionBar?.show()
    }

}
