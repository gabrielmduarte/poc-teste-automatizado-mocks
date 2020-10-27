package br.com.gabriel.mocktest.infra.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.gabriel.mocktest.domain.Bid;
import br.com.gabriel.mocktest.domain.Auction;
import br.com.gabriel.mocktest.domain.User;
import br.com.gabriel.mocktest.repository.AuctionRepository;

public class AuctionDao implements AuctionRepository {

	private Connection connection;

	public AuctionDao() {
		try {
			this.connection = DriverManager.getConnection(
					"jdbc:mysql://localhost/mocks", "root", "biel0507");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Calendar date(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	public void save(Auction auction) {
		try {
			String sql = "INSERT INTO LEILAO (DESCRICAO, DATA, ENCERRADO) VALUES (?,?,?);";
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, auction.getDescription());
			ps.setDate(2, new java.sql.Date(auction.getDate().getTimeInMillis()));
			ps.setBoolean(3, auction.isEnded());
			
			ps.execute();
			
			ResultSet generatedKeys = ps.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            auction.setId(generatedKeys.getInt(1));
	        }
			
			for(Bid lance : auction.getBids()) {
				sql = "INSERT INTO LANCES (LEILAO_ID, USUARIO_ID, VALOR) VALUES (?,?,?);";
				PreparedStatement ps2 = connection.prepareStatement(sql);
				ps2.setInt(1, auction.getId());
				ps2.setInt(2, lance.getUser().getId());
				ps2.setDouble(3, lance.getAmount());
				
				ps2.execute();
				ps2.close();
				
			}
			
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public List<Auction> finished() {
		return porEncerrado(true);
	}
	
	public List<Auction> currents() {
		return porEncerrado(false);
	}
	
	private List<Auction> porEncerrado(boolean status) {
		try {
			String sql = "SELECT * FROM LEILAO WHERE ENCERRADO = " + status + ";";
			
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			List<Auction> leiloes = new ArrayList<Auction>();
			while(rs.next()) {
				Auction auction = new Auction(rs.getString("descricao"), date(rs.getDate("data")));
				auction.setId(rs.getInt("id"));
				if(rs.getBoolean("encerrado")) auction.ends();
				
				String sql2 = "SELECT VALOR, NOME, U.ID AS USUARIO_ID, L.ID AS LANCE_ID FROM LANCES L INNER JOIN USUARIO U ON U.ID = L.USUARIO_ID WHERE LEILAO_ID = " + rs.getInt("id");
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				
				while(rs2.next()) {
					User user = new User(rs2.getInt("id"), rs2.getString("nome"));
					Bid lance = new Bid(user, rs2.getDouble("valor"));
					
					auction.propose(lance);
				}
				rs2.close();
				ps2.close();
				
				leiloes.add(auction);
				
			}
			rs.close();
			ps.close();
			
			return leiloes;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(Auction auction) {
		
		try {
			String sql = "UPDATE LEILAO SET DESCRICAO=?, DATA=?, ENCERRADO=? WHERE ID = ?;";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, auction.getDescription());
			ps.setDate(2, new java.sql.Date(auction.getDate().getTimeInMillis()));
			ps.setBoolean(3, auction.isEnded());
			ps.setInt(4, auction.getId());

			ps.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int x() { return 10; }
}
