package io.entertainmentgo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.entertainmentgo.entity.Title;
import io.entertainmentgo.exception.NoTitleFoundException;
import io.entertainmentgo.exception.UnknownFilterException;
import io.entertainmentgo.exception.UnknownSortException;
import io.entertainmentgo.repository.TitleRepository;

@Service
@Transactional
public class TitleServiceImpl implements TitleService {

	@Autowired
	private TitleRepository titleRepository;

	@Override
	public List<Title> findAllTitles() {
		List<Title> allTitles = (List<Title>) titleRepository.findAll();
		return allTitles;
	}

	@Override
	public List<Title> findTopTitles(Double toprating) {
		List<Title> topTitles = titleRepository.findByImdbRatingGreaterThanOrderByImdbRatingDesc(toprating);
		return topTitles;
	}

	@Override
	public List<Title> findTopTitles(double toprating, String type) {
		List<Title> topTitles = titleRepository.findByImdbRatingAndTypeGreaterThanOrderByImdbRatingDesc(toprating,
				type);
		return topTitles;
	}

	@Override
	public List<Title> findByType(String basedOn, String value) throws UnknownFilterException {
		List<Title> typeTitles = null;
		if (basedOn.equals("type")) {
			typeTitles = titleRepository.findByType(value);
			return typeTitles;
		} else if (basedOn.equals("genre")) {
			typeTitles = titleRepository.findByGenre_genre(value);
			return typeTitles;
		} else if (basedOn.equals("year")) {
			typeTitles = titleRepository.findByYear(Integer.parseInt(value));
			return typeTitles;
		} else {
			throw new UnknownFilterException("No such Filter Found");
		}

	}

	@Override
	public List<Title> findBySort(String sortBy) throws UnknownSortException {
		List<Title> sortTitles = null;

		if (sortBy.equals("imdbrating")) {
			sortTitles = titleRepository.findByOrderByImdbRatingDesc();
			return sortTitles;

		} else if (sortBy.equals("imdbvote")) {
			sortTitles = titleRepository.findByOrderByImdbVotesDesc();
			return sortTitles;
		} else if (sortBy.equals("year")) {
			sortTitles = titleRepository.findByOrderByYearDesc();
			return sortTitles;
		} else {
			throw new UnknownSortException("No such sort field Found");
		}

		
	}

	@Override
	public Title findTitle(String titleId) throws NoTitleFoundException {
		Title existingTitle = titleRepository.findOne(titleId);
		if(existingTitle == null){
			throw new NoTitleFoundException("No title found with the given ID");
		}else{
			return existingTitle;
		}
	
	}

}
