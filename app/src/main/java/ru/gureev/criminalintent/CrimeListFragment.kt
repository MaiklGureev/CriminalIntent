package ru.gureev.criminalintent

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    companion object {
        fun newInstance() = CrimeListFragment()
    }

    private val viewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    private lateinit var crimeRecyclerView: RecyclerView
    private lateinit var crimesListIsEmptyTextVie: TextView
    private lateinit var addNewCrimeButton: Button
    private var crimeAdapter: CrimeAdapter? = CrimeAdapter(emptyList())
    private var callbacks: Callbacks? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.crime_list_fragment, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimesListIsEmptyTextVie = view.findViewById(R.id.crime_text_view_rv_is_empty) as TextView
        addNewCrimeButton = view.findViewById(R.id.add_new_crime_button) as Button
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = crimeAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeListLiveData.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                updateUI(list)
                crimesListIsEmptyTextVie.visibility = View.GONE
                addNewCrimeButton.visibility = View.GONE
                crimeRecyclerView.visibility = View.VISIBLE
                if (list.isEmpty()) {
                    crimesListIsEmptyTextVie.visibility = View.VISIBLE
                    addNewCrimeButton.visibility = View.VISIBLE
                    crimeRecyclerView.visibility = View.GONE
                }
            }
        })
    }

    fun updateUI(crimes: List<Crime>) {
        crimeAdapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = crimeAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var crime: Crime
        private var titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private var dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private var isSolvedImageView: ImageView = itemView.findViewById(R.id.crime_image_is_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            isSolvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }

            val pattern = "EEE, MMM d, ''yy"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date = simpleDateFormat.format(this.crime.date)
            dateTextView.text = date
        }

        override fun onClick(v: View?) {
            callbacks?.onCrimeSelected(crimeId = crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view =
                when (viewType) {
                    1 -> {
                        layoutInflater.inflate(R.layout.list_item_extra_crime, parent, false)
                    }
                    else -> layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                }
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes.get(position)
            holder.bind(crime)
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun getItemViewType(position: Int): Int {
            return crimes.get(position).requiresPolice
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    fun addNewCrime() {
        val crime = Crime()
        viewModel.addCrime(crime)
        callbacks?.onCrimeSelected(crimeId = crime.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                addNewCrime()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}