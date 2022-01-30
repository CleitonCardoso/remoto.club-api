package com.remototech.remototechapi.services.crawlers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.CompensationType;
import com.remototech.remototechapi.entities.Job;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemotarCrawler {

	private static final String REMOTAR_URL = "https://remotar.com.br";

	public List<Job> extractJobs(int pagesNumber) throws IOException {
		List<Job> jobs = new ArrayList<>();
		IntStream.rangeClosed( 1, pagesNumber ).forEach( (i) -> {
			Document document;
			try {
				document = Jsoup.connect( REMOTAR_URL + "/job-category/tecnologia-dev/page/" + i ).get();

				Elements elements = document.getElementsByClass( "posts-loop-content" );
				elements.parallelStream().forEach( element -> {
					Elements articleTags = element.getElementsByTag( "article" );
					if (!articleTags.isEmpty()) {
						articleTags.parallelStream().forEach( articleTag -> {
							Elements anchorTags = articleTag.getElementsByTag( "a" );
							Element anchorTag = anchorTags.get( 0 );
							Job job;
							try {
								job = buildJob( anchorTag.attr( "href" ) );
								jobs.add( job );
							} catch (Exception e) {
								log.error( "Job não foi possível de ser registrado", e );
							}
						} );
					}
				} );
			} catch (Exception e) {
				log.error( "Não foi possível extrair os jobs do site REMOTAR", e );
			}
		} );
		return jobs;
	}

	private Job buildJob(String jobUrl) throws IOException {
		Job job = Job.builder().thirdPartyUrl( jobUrl ).build();
		Document document = Jsoup.connect( jobUrl ).get();
		fillJobTitle( job, document );
		fillContractType( job, document );
		fillCompany( job, document );
		fillDescription( job, document );
		job.setCompensationType( CompensationType.PER_MONTH );
		job.setCreatedDate( LocalDateTime.now() );

		return job;
	}

	private void fillDescription(Job job, Document document) {
		Element descriptionElement = document.selectFirst( "div.description" );
		descriptionElement.selectFirst( "p > a.btn" ).remove();
		job.setDescription( descriptionElement.text() );
	}

	private void fillCompany(Job job, Document document) {
		Element jobCompanyElement = document.selectFirst( "span.job-company > a > span" );
		job.setCompany( jobCompanyElement.text() );
	}

	private void fillContractType(Job job, Document document) {
		Evaluator evaluator = new Evaluator() {
			@Override
			public boolean matches(Element root, Element element) {
				String tag = element.tagName();
				if (tag.equals( "span" )) {
					Elements siblingElements = element.siblingElements();
					for (Element siblingElement : siblingElements) {
						String siblingClass = siblingElement.attr( "class" );
						if (siblingClass != null && siblingClass.equals( "fa fa-bookmark" )) {
							return true;
						}
					}
				}
				return false;
			}
		};
		Element contractTypeElement = document.selectFirst( evaluator );
		if (contractTypeElement != null)
			job.setContractType( contractTypeElement.text() );
	}

	private void fillJobTitle(Job job, Document document) {
		Element pageTitle = document.selectFirst( "h1.page-title > span" );
		job.setTitle( pageTitle.text() );
	}

	public static void main(String[] args) throws IOException {
		RemotarCrawler crawler = new RemotarCrawler();
		crawler.extractJobs( 10 );
	}
}
