package it.polito.tdp.crimes.model;

import java.util.List;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	public List<String> listAllOffenseCategoryId() {
		return EventsDao.listAllOffenseCategoryId();
	}

	public List<Integer> listAllMonths() {
		return EventsDao.listAllMonths();
	}

}
