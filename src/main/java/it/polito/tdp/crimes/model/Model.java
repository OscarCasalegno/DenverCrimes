package it.polito.tdp.crimes.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	private Graph<String, DefaultWeightedEdge> graph;

	public List<String> listAllOffenseCategoryId() {
		return EventsDao.listAllOffenseCategoryId();
	}

	public List<Integer> listAllMonths() {
		return EventsDao.listAllMonths();
	}

	public void creaGrafo(String OffenseCategoryId, Integer month) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		// vertici
		Graphs.addAllVertices(this.graph, EventsDao.getTypeByCategoryAndMonth(OffenseCategoryId, month));

		System.out.println(this.graph);
	}

}
