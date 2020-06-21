package com.mitocode.controller;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.chart.PieChartModel;

import com.mitocode.service.ISeguidorService;
import com.mitocode.util.ReporteSeguidor;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class ReporteBean implements Serializable
{
	@Inject
	private ISeguidorService seguidorService;
	
	private List<ReporteSeguidor> lista;
	private PieChartModel pieModel1;
	
	@PostConstruct
	public void init() {
		this.listarSeguidores();
		this.crearPieModel();
	}
	
	public void crearPieModel() {
		this.pieModel1 = new PieChartModel();
		
		this.lista.forEach(x->{
			this.pieModel1.set(x.getPublicador(), x.getCantidad());
		});
		
		this.pieModel1.setTitle("Cantidad de Seguidores");
		this.pieModel1.setLegendPosition("w");
		this.pieModel1.setShowDataLabels(true);
	}
	
	private void listarSeguidores() {
		try {
			this.lista = this.seguidorService.listarSeguidores();
		}catch(Exception e) {
			
		}
	}
	
	public void generarReporte()
	{
		try {
			Map<String, Object> parametros = new HashMap<>();
			//parametros.put("","");
			
			File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/mini_blog.jasper"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.lista));
			
			HttpServletResponse resp = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			resp.addHeader("Content-disposition", "attachment; filename-mini-blog.pdf");
			ServletOutputStream stream = resp.getOutputStream();
			
			JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
			
			stream.flush();
			stream.close();
			FacesContext.getCurrentInstance().responseComplete();
		}catch(Exception e) {
			
		}
	}
	
	public void verPDF()
	{
		try {
			File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/mini_blog.jasper"));
			
			byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), null, new JRBeanCollectionDataSource(this.lista));
			
			HttpServletResponse resp = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			resp.setContentType("application/pdf");
			resp.setContentLength(bytes.length);
			
			ServletOutputStream stream = resp.getOutputStream();
			stream.write(bytes, 0, bytes.length);
			
			stream.flush();
			stream.close();
			FacesContext.getCurrentInstance().responseComplete();
		}catch(Exception e) {
			
		}
	}

	public List<ReporteSeguidor> getLista() {
		return lista;
	}

	public void setLista(List<ReporteSeguidor> lista) {
		this.lista = lista;
	}

	public PieChartModel getPieModel1() {
		return pieModel1;
	}

	public void setPieModel1(PieChartModel pieModel1) {
		this.pieModel1 = pieModel1;
	}
}
