package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.Pair;

public class EventsDao {

	public static List<Event> listAllEvents() {
		String sql = "SELECT * FROM events";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Event> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"), res.getInt("offense_code"),
							res.getInt("offense_code_extension"), res.getString("offense_type_id"),
							res.getString("offense_category_id"), res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"), res.getDouble("geo_lon"), res.getDouble("geo_lat"),
							res.getInt("district_id"), res.getInt("precinct_id"), res.getString("neighborhood_id"),
							res.getInt("is_crime"), res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List<String> listAllOffenseCategoryId() {

		String sql = "SELECT DISTINCT offense_category_id " + "FROM events";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<String> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(res.getString("offense_category_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static List<Integer> listAllMonths() {

		String sql = "SELECT DISTINCT MONTH(reported_date) " + "FROM EVENTS " + "ORDER BY MONTH(reported_date)";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Integer> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(res.getInt("MONTH(reported_date)"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static List<String> getTypeByCategoryAndMonth(String offenseCategoryId, Integer month) {
		String sql = "SELECT DISTINCT offense_type_id " + "FROM EVENTS "
				+ "WHERE MONTH(reported_date)= ? AND offense_category_id= ?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, month);
			st.setString(2, offenseCategoryId);

			List<String> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(res.getString("offense_type_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Pair> getPairs(String offenseCategoryId, Integer month) {
		String sql = "SELECT e1.offense_type_id, e2.offense_type_id,COUNT(DISTINCT e1.neighborhood_id) "
				+ "FROM events AS e1, events AS e2 "
				+ "WHERE e1.neighborhood_id=e2.neighborhood_id AND e1.offense_type_id<e2.offense_type_id AND "
				+ "MONTH(e1.reported_date)=? AND MONTH(e2.reported_date)=? AND e1.offense_category_id=? AND e2.offense_category_id=? "
				+ "group by e1.offense_type_id, e2.offense_type_id";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, month);
			st.setInt(2, month);
			st.setString(3, offenseCategoryId);
			st.setString(4, offenseCategoryId);

			List<Pair> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Pair(res.getString("e1.offense_type_id"), res.getString("e2.offense_type_id"),
							res.getInt("COUNT(DISTINCT e1.neighborhood_id)")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
