package dao;

import config.Database;
import java.sql.*;
import java.util.*;
import model.Pendaftaran;
import util.LookupItem;

public class PendaftaranDAO {
    private Pendaftaran map(ResultSet r)throws SQLException{Pendaftaran x=new Pendaftaran();x.setIdPendaftaran(r.getInt("id_pendaftaran"));x.setIdPeserta(r.getInt("id_peserta"));x.setIdKelas(r.getInt("id_kelas"));x.setTanggalDaftar(r.getDate("tanggal_daftar"));x.setStatus(r.getString("status"));x.setNim(r.getString("nim"));x.setNamaPeserta(r.getString("nama_peserta"));x.setNamaKelas(r.getString("nama_kelas"));x.setNamaKursus(r.getString("nama_kursus"));return x;}
    public boolean insert(Pendaftaran x)throws SQLException{String s="INSERT INTO pendaftaran(id_peserta,id_kelas,tanggal_daftar,status) VALUES(?,?,?,?)";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(s)){bind(p,x);return p.executeUpdate()==1;}}
    public boolean update(Pendaftaran x)throws SQLException{String s="UPDATE pendaftaran SET id_peserta=?,id_kelas=?,tanggal_daftar=?,status=? WHERE id_pendaftaran=?";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(s)){bind(p,x);p.setInt(5,x.getIdPendaftaran());return p.executeUpdate()>0;}}
    private void bind(PreparedStatement p,Pendaftaran x)throws SQLException{p.setInt(1,x.getIdPeserta());p.setInt(2,x.getIdKelas());p.setDate(3,x.getTanggalDaftar());p.setString(4,x.getStatus());}
    public boolean delete(int id)throws SQLException{try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM pendaftaran WHERE id_pendaftaran=?")){p.setInt(1,id);return p.executeUpdate()>0;}}
    public List<Pendaftaran> findAll()throws SQLException{return search("");}
    public List<Pendaftaran> search(String q)throws SQLException{List<Pendaftaran> o=new ArrayList<>();String s="SELECT p.id_pendaftaran,p.id_peserta,p.id_kelas,p.tanggal_daftar,p.status,ps.nim,ps.nama AS nama_peserta,k.nama_kelas,kr.nama_kursus FROM pendaftaran p JOIN peserta ps ON ps.id_peserta=p.id_peserta JOIN kelas k ON k.id_kelas=p.id_kelas JOIN kursus kr ON kr.id_kursus=k.id_kursus WHERE ps.nim LIKE ? OR ps.nama LIKE ? OR k.nama_kelas LIKE ? OR p.status LIKE ? ORDER BY p.id_pendaftaran DESC";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(s)){String z="%"+(q==null?"":q.trim())+"%";for(int i=1;i<=4;i++)p.setString(i,z);try(ResultSet r=p.executeQuery()){while(r.next())o.add(map(r));}}return o;}
    public List<LookupItem> lookup()throws SQLException{List<LookupItem> o=new ArrayList<>();String s="SELECT p.id_pendaftaran,ps.nama,k.nama_kelas FROM pendaftaran p JOIN peserta ps ON ps.id_peserta=p.id_peserta JOIN kelas k ON k.id_kelas=p.id_kelas ORDER BY p.id_pendaftaran DESC";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(s);ResultSet r=p.executeQuery()){while(r.next())o.add(new LookupItem(r.getInt(1),"#"+r.getInt(1)+" - "+r.getString(2)+" / "+r.getString(3)));}return o;}
}