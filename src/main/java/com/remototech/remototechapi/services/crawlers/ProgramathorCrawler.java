package com.remototech.remototechapi.services.crawlers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.CompensationType;
import com.remototech.remototechapi.entities.Job;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProgramathorCrawler {

	private static final String PROGRAMATHOR_URL = "https://programathor.com.br";

	public List<Job> extractJobs(int pagesNumber) throws IOException {
		List<Job> jobs = new ArrayList<>();
		IntStream.rangeClosed( 1, pagesNumber ).forEach( (i) -> {
			Document document;
			try {
				document = Jsoup.connect( PROGRAMATHOR_URL + "/jobs-city/remoto?page=" + i ).get();

				Elements elements = document.getElementsByClass( "cell-list" );
				elements.parallelStream().forEach( element -> {
					if (isValidElement( element )) {
						Elements anchorTags = element.getElementsByTag( "a" );
						if (!anchorTags.isEmpty()) {
							Element anchor = anchorTags.get( 0 );
							Job job;
							try {
								job = buildJob( PROGRAMATHOR_URL + anchor.attr( "href" ) );
								jobs.add( job );
							} catch (Exception e) {
								log.error( "Job não foi possível de ser registrado", e );
							}
						}
					}
				} );
			} catch (Exception e) {
				log.error( "Não foi possível extrair os jobs do site PROGRAMATHOR", e );
			}
		} );
		return jobs;
	}

	private Job buildJob(String jobUrl) throws IOException {
		Job job = Job.builder().build();
		Document document = Jsoup.connect( jobUrl ).get();

		clearScriptTags( document );
		fillTitle( job, document );
		fillCompany( job, document );
		fillDescription( job, document );
		fillSalary( job, document );
		fillContractType( job, document );
		fillExperienceType( job, document );
		job.setCompensationType( CompensationType.PER_MONTH );

		return job;
	}

	private void fillCompany(Job job, Document document) {
		Elements companyElements = document.select( "div.wrapper-content-job-show > h2" );
		if (!companyElements.isEmpty()) {
			Element title = companyElements.get( 0 );
			job.setCompany( title.text() );
		}
	}

	private void fillExperienceType(Job job, Document document) {
		job.setExperienceRequired( getPropertyFromPage( document, "fa fa-signal", "a" ).toUpperCase().replace( "Ê", "E" ) );
	}

	private void fillContractType(Job job, Document document) {
		job.setContractType( getPropertyFromPage( document, "far fa-file-alt", "a" ).toUpperCase() );
	}

	private String getPropertyFromPage(Document document, String referenceClassName, String referenceTag) {
		Elements elements = document.getElementsByClass( referenceClassName );
		if (!elements.isEmpty()) {
			Element element = elements.get( 0 );
			Element elementP = element.parent();
			if (referenceTag != null) {
				Elements targetElements = elementP.getElementsByTag( referenceTag );
				if (!targetElements.isEmpty()) {
					return targetElements.get( 0 ).text();
				}
			} else {
				return elementP.text();
			}

		}
		return null;
	}

	private void fillSalary(Job job, Document document) {
		String salaryText = getPropertyFromPage( document, "far fa-money-bill-alt", null );
		String numberSalary = salaryText.replace( "Salário: Até R$", "" )
				.replace( "Salário: Acima de R$", "" )
				.replace( ".", "" );
		if (!StringUtils.isEmpty( numberSalary )) {
			BigDecimal salary = BigDecimal.valueOf( Long.valueOf( numberSalary ) );
			job.setSalary( salary );
		}
	}

	private void clearScriptTags(Document document) {
		Elements scripts = document.getElementsByTag( "script" );
		scripts.remove();
	}

	private void fillTitle(Job job, Document document) {
		Elements titles = document.select( "div.wrapper-header-job-show > div > h1" );
		if (!titles.isEmpty()) {
			Element title = titles.get( 0 );
			job.setTitle( title.text() );
		}
	}

	private void fillDescription(Job job, Document document) {
		Elements descriptions = document.getElementsByClass( "line-height-2-4" );
		if (!descriptions.isEmpty()) {
			Element description = descriptions.get( 0 );
			String baseText = description.wholeText().trim()
					.replace( "  ", "" )
					.replaceAll( "[\t]", "" )
					.replace( "\n\n\n", "" );
			if (baseText.length() > 2048)
				baseText = baseText.substring( 0, 2048 );
			job.setDescription( baseText );
		}
	}

	private boolean isValidElement(Element element) {
		Elements moneyIElements = element.getElementsByClass( "fa-money-bill-alt" );
		if (moneyIElements.isEmpty()) {
			return false;
		}
		Element moneyI = moneyIElements.get( 0 );
		Element moneySpam = moneyI.parent();
		return !StringUtil.isBlank( moneySpam.text() );
	}

}
