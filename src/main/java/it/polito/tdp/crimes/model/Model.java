package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.HamiltonianCycleAlgorithm;
import org.jgrapht.alg.tour.RandomTourTSP;
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

	public void creaGrafo(String offenseCategoryId, Integer month) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		// vertici
		Graphs.addAllVertices(this.graph, EventsDao.getTypeByCategoryAndMonth(offenseCategoryId, month));

		// lati
		for (Pair p : EventsDao.getPairs(offenseCategoryId, month)) {
			this.graph.setEdgeWeight(this.graph.addEdge(p.getV1(), p.getV2()), p.getPeso());
		}

	}

	public List<DefaultWeightedEdge> getEdges() {

		double somma = 0;
		for (DefaultWeightedEdge e : this.graph.edgeSet()) {
			somma += this.graph.getEdgeWeight(e);
		}

		double media = somma / this.graph.edgeSet().size();

		List<DefaultWeightedEdge> lista = new ArrayList<>();

		for (DefaultWeightedEdge e : this.graph.edgeSet()) {
			if (this.graph.getEdgeWeight(e) > media) {
				lista.add(e);
			}
		}

		return lista;
	}

	public double getW(DefaultWeightedEdge e) {
		return this.graph.getEdgeWeight(e);
	}

	public List<String> path(DefaultWeightedEdge e) {
		String source = this.graph.getEdgeSource(e);
		String target = this.graph.getEdgeTarget(e);

		// Genero un grafo ugauale al primo con il peso degli archi uguale a 0
		Graph<String, DefaultWeightedEdge> doppio = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		Graphs.addAllVertices(doppio, this.graph.vertexSet());

		for (DefaultWeightedEdge ed : this.graph.edgeSet()) {
			doppio.setEdgeWeight(doppio.addEdge(this.graph.getEdgeSource(ed), this.graph.getEdgeTarget(ed)), 0);
		}

		// rimuovo i vertici senza connessioni
		Set<String> rim = new HashSet<>();
		for (String d : doppio.vertexSet()) {
			if (doppio.degreeOf(d) == 0)
				rim.add(d);
		}

		doppio.removeAllVertices(rim);

		// rendo il grafo completo
		for (String s : doppio.vertexSet()) {
			for (String d : doppio.vertexSet()) {
				if (!s.equals(d))
					Graphs.addEdgeWithVertices(doppio, s, d, 10);
			}
		}

		// trovo un ciclo euleriano che inizi da source e abbia come penultimo vertice
		// il target
		HamiltonianCycleAlgorithm<String, DefaultWeightedEdge> alg = new RandomTourTSP<>();
		GraphPath<String, DefaultWeightedEdge> path;

		do {
			path = alg.getTour(doppio);
		} while (!path.getStartVertex().equals(source)
				|| !path.getVertexList().get(path.getVertexList().size() - 2).equals(target));

		// aggiusto la lista per renderlo un percorso e non un ciclo
		List<String> list = path.getVertexList();
		list.remove(list.size() - 1);

		return list;

	}

}
