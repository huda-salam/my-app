DECLARE @Tahun smallint, @Kd_Urusan varchar(3), @Kd_Bidang varchar(3), @Kd_Unit varchar(3), @Kd_Sub varchar(3), @D1 DATE
SET @Tahun = 2021
SET @Kd_Urusan = ?
SET @Kd_Bidang  = ?
SET @Kd_Unit  = ?
SET @Kd_Sub = ?
SET @D1 = CAST(CAST(@Tahun as VARCHAR) + '/12/31' as DATE)
 
IF ISNULL(@Kd_Urusan, '') = '' SET @Kd_Urusan = '%' 
IF ISNULL(@Kd_Bidang, '') = '' SET @Kd_Bidang = '%' 
IF ISNULL(@Kd_Unit, '') = '' SET @Kd_Unit = '%' 
IF ISNULL(@Kd_Sub, '') = '' SET @Kd_Sub = '%' 
 
SELECT  u90.Kd_Urusan,
		u90.Nm_Urusan,

		convert(varchar, u90.kd_Urusan) + '.' + RIGHT('0' + convert(varchar, b90.kd_bidang), 2) AS Kd_Bidang_Gab, 
		b90.Nm_Bidang,
		
		convert(varchar, u90.kd_Urusan) + '.' + RIGHT('0' + convert(varchar, b90.kd_bidang), 2) + ' . ' + u.kd_unit90 AS Kd_Unit_Gab,
		u.Nm_Unit Nm_Unit_Gab, 
			
		CONVERT(varchar, b90.Kd_Urusan) + '.' + RIGHT('0' + CONVERT(varchar, b90.Kd_Bidang), 2) + ' . ' + u.kd_unit90 + ' . ' + CASE WHEN k90.kd_program = 0 THEN 'xx' ELSE RIGHT('0' + CONVERT(varchar, k90.kd_program), 2) END AS Kd_Prog_Gab, 
		p90.Nm_Program,
		
		CONVERT(varchar, b90.Kd_Urusan) + '.' + RIGHT('0' + CONVERT(varchar, b90.Kd_Bidang), 2) + ' . ' + u.kd_unit90 + ' . ' + CASE WHEN k90.kd_program = 0 THEN 'xx' ELSE RIGHT('0' + CONVERT(varchar, k90.kd_program), 2) END + ' . ' + CASE WHEN k90.kd_kegiatan = '0.00' THEN 'x.xx' ELSE k90.kd_kegiatan END AS Kd_Keg_Gab,
		k90.Nm_Kegiatan,
		
		CONVERT(varchar, b90.Kd_Urusan) + '.' + RIGHT('0' + CONVERT(varchar, b90.Kd_Bidang), 2) + ' . ' + u.kd_unit90 + ' . ' + CASE WHEN k90.kd_program = 0 THEN 'xx' ELSE RIGHT('0' + CONVERT(varchar, k90.kd_program), 2) END + ' . ' + CASE WHEN k90.kd_kegiatan = '0.00' THEN 'x.xx' ELSE k90.kd_kegiatan END + ' . ' + CASE WHEN sk90.kd_sub_kegiatan = 0 THEN 'xx' ELSE RIGHT('0' + CONVERT(varchar, sk90.kd_sub_kegiatan), 2) END AS Kd_Sub_Keg_Gab, 
		sk90.Nm_Sub_Kegiatan,
		anggaran_51,
		anggaran_52,
		anggaran_53,
		anggaran_54,
		realisasi_51,
		realisasi_52,
		realisasi_53,
		realisasi_54
		
 FROM (       
		SELECT Tahun, Kd_Urusan, Kd_Bidang, Kd_Unit, Kd_Sub,  
			Kd_Prog, Id_Prog, Kd_Keg,  			     
			SUM(CASE WHEN Kd_Rek90_2 = 1 THEN Anggaran ELSE 0 END) anggaran_51, 
			SUM(CASE WHEN Kd_Rek90_2 = 2 THEN Anggaran ELSE 0 END) anggaran_52, 
			SUM(CASE WHEN Kd_Rek90_2 = 3 THEN Anggaran ELSE 0 END) anggaran_53, 
			SUM(CASE WHEN Kd_Rek90_2 = 4 THEN Anggaran ELSE 0 END) anggaran_54, 
			SUM(CASE WHEN Kd_Rek90_2 = 1 THEN Realisasi ELSE 0 END) realisasi_51, 
			SUM(CASE WHEN Kd_Rek90_2 = 2 THEN Realisasi ELSE 0 END) realisasi_52, 
			SUM(CASE WHEN Kd_Rek90_2 = 3 THEN Realisasi ELSE 0 END) realisasi_53, 
			SUM(CASE WHEN Kd_Rek90_2 = 4 THEN Realisasi ELSE 0 END) realisasi_54			
		FROM      
		(  
			SELECT a.Tahun, a.Kd_Urusan, a.Kd_Bidang, a.Kd_Unit, a.Kd_Sub,  
				a.Kd_Prog, a.Id_Prog, a.Kd_Keg,  kd_rek90_1, kd_rek90_2,
				
				Total Anggaran, 0 Realisasi  
			FROM Ta_RASK_Arsip a  
			INNER JOIN Ref_Rek_Mapping b ON a.Kd_Rek_1 = b.Kd_Rek_1 AND a.Kd_Rek_2 = b.Kd_Rek_2 AND a.Kd_Rek_3 = b.Kd_Rek_3 AND a.Kd_Rek_4 = b.Kd_Rek_4 AND a.Kd_Rek_5 = b.Kd_Rek_5  
			WHERE a.Kd_Perubahan = (SELECT MAX(Kd_Perubahan) FROM Ta_RASK_Arsip_Perubahan WHERE Tgl_Perda <= @D1)  
			AND a.Tahun = @Tahun AND Kd_Urusan LIKE @Kd_Urusan AND Kd_Bidang LIKE @Kd_Bidang AND Kd_Unit LIKE @Kd_Unit AND Kd_Sub LIKE @Kd_Sub  
			AND a.kd_rek_1 = 5
			
			UNION ALL  
             
			SELECT a.Tahun, a.Kd_Urusan, a.Kd_Bidang, a.Kd_Unit, a.Kd_Sub,  
				b.Kd_Prog, b.Id_Prog, b.Kd_Keg, kd_rek90_1, kd_rek90_2,				
				0, Debet-Kredit   
			FROM Ta_JurnalSemuaAk a  
			INNER JOIN Ta_JurnalSemuaAk_Rinc b ON a.Tahun = b.Tahun AND a.Kd_Source = b.Kd_Source AND a.No_Bukti = b.No_Bukti         
			WHERE b.Kd_Rek90_1 = 5 
			AND a.Tgl_Bukti <= @D1  
			AND ((b.Kd_SKPD = 1 AND a.Posting = 1) OR (b.Kd_SKPD = 2 AND a.Posting_SKPKD = 1))  
			AND NOT b.Kd_Jurnal IN (8,10)  
			AND a.Tahun = @Tahun AND Kd_Urusan LIKE @Kd_Urusan AND Kd_Bidang LIKE @Kd_Bidang AND Kd_Unit LIKE @Kd_Unit AND Kd_Sub LIKE @Kd_Sub  
		) x  
	WHERE x.Anggaran > 0 OR x.Realisasi <>0  
	GROUP BY Tahun, Kd_Urusan, Kd_Bidang, Kd_Unit, Kd_Sub,  
		Kd_Prog, Id_Prog, Kd_Keg
) a 
INNER JOIN Ref_Unit             u   ON u.Kd_Urusan = a.Kd_Urusan AND u.Kd_Bidang = a.Kd_Bidang AND u.Kd_Unit = a.Kd_Unit 
INNER JOIN Ta_Program           p   ON p.Kd_Urusan = a.Kd_Urusan AND p.Kd_Bidang = a.Kd_Bidang AND p.Kd_Unit = a.Kd_Unit AND p.Kd_Sub = a.Kd_Sub AND p.Kd_Prog = a.Kd_Prog AND p.Id_Prog = a.Id_Prog AND p.Tahun = @Tahun
INNER JOIN Ref_Kegiatan_Mapping map ON map.Kd_Prog = a.Kd_Prog AND map.Kd_Keg = a.Kd_Keg AND map.Kd_Urusan = p.Kd_Urusan1 AND map.Kd_Bidang = p.Kd_Bidang1 
INNER JOIN Ref_Sub_Kegiatan90   sk90 ON sk90.Kd_Urusan = map.Kd_Urusan90 AND sk90.Kd_Bidang = map.Kd_Bidang90 AND sk90.Kd_Program = map.Kd_Program90 AND sk90.Kd_Kegiatan = map.Kd_Kegiatan90 AND sk90.Kd_Sub_Kegiatan = map.Kd_Sub_Kegiatan 
INNER JOIN Ref_Kegiatan90       k90 ON k90.Kd_Urusan = sk90.Kd_Urusan AND k90.Kd_Bidang = sk90.Kd_Bidang AND k90.Kd_Program = sk90.Kd_Program AND k90.Kd_Kegiatan = sk90.Kd_Kegiatan 
INNER JOIN Ref_Program90        p90 ON p90.Kd_Urusan = sk90.Kd_Urusan AND p90.Kd_Bidang = sk90.Kd_Bidang AND p90.Kd_Program = sk90.Kd_Program 
INNER JOIN ref_bidang_mapping   map_b ON map_b.kd_urusan = a.kd_urusan AND map_b.kd_bidang = a.kd_bidang
INNER JOIN Ref_Bidang90         b90 ON b90.Kd_Urusan = map_b.kd_urusan90 AND b90.Kd_Bidang = map_b.kd_bidang90 
INNER JOIN Ref_Urusan90         u90 ON u90.Kd_Urusan = b90.Kd_Urusan 

ORDER BY 
	Kd_Bidang_Gab, 
	Kd_Prog_Gab, 
	Kd_Keg_Gab, 
	Kd_Sub_Keg_Gab