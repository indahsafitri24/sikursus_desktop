package dao;

import config.Database;
import java.sql.*;
import java.util.*;
import model.Pembayaran;

public class PembayaranDAO {
    private Pembayaran map(ResultSet r)throws SQLException{Pembayaran x=new Pembayaran();x.setIdPembayaran(r.getInt("id_pembayaran"));x.setIdPendaftaran(r.getInt("id_pendaftaran"));x.setTanggalBayar(r.getDate("tanggal_bayar"));x.setNominal(r.getBigDecimal("nominal"));x.setMetode(r.getString("metode"));x.setStatus(r.getString("status"));x.setNamaPeserta(r.getString("nama_peserta"));x.setNamaKelas(r.getString("nama_kelas"));return x;}
    public boolean insert(Pembayaran x)throws SQLException{String s="INSERT INTO pembayaran(id_pendaftaran,tanggal_bayar,nominal,metode,status) VALUES(?,?,?,?,?)";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(s)){bind(p,x);return p.executeUpdate()==1;}}
    public boolean update(Pembayaran x)throws SQLException{String s="UPDATE pembayaran SET id_pendaftaran=?,tanggal_bayar=?,nominal=?,metode=?,status=? WHERE id_pembayaran=?";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(s)){bind(p,x);p.setInt(6,x.getIdPembayaran());return p.executeUpdate()>0;}}
    private void bind(PreparedStatement p,Pembayaran x)throws SQLException{p.setInt(1,x.getIdPendaftaran());p.setDate(2,x.getTanggalBayar());p.setBigDecimal(3,x.getNominal());p.setString(4,x.getMetode());p.setString(5,x.getStatus());}
    public boolean delete(int id)throws SQLException{try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM pembayaran WHERE id_pembayaran=?")){p.setInt(1,id);return p.executeUpdate()>0;}}
    public List<Pembayaran> findAll()throws SQLException{return search("");}
    public List<Pembayaran> search(String q)throws SQLException{List<Pembayaran> o=new ArrayList<>();String s="SELECT pb.id_pembayaran,pb.id_pendaftaran,pb.tanggal_bayar,pb.nominal,pb.metode,pb.status,ps.nama AS nama_peserta,k.nama_kelas FROM pembayaran pb JOIN pendaftaran p ON p.id_pendaftaran=pb.id_pendaftaran JOIN peserta ps ON ps.id_peserta=p.id_peserta JOIN kelas k ON k.id_kelas=p.id_kelas WHERE ps.nama LIKE ? OR k.nama_kelas LIKE ? OR pb.metode LIKE ? OR pb.status LIKE ? ORDER BY pb.id_pembayaran DESC";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(s)){String z="%"+(q==null?"":q.trim())+"%";for(int i=1;i<=4;i++)p.setString(i,z);try(ResultSet r=p.executeQuery()){while(r.next())o.add(map(r));}}return o;}
}