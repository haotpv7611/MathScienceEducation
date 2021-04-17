package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ExerciseResponseDTO;
import com.example.demo.dtos.LessonScoreViewDTO;
import com.example.demo.dtos.ScoreResponseDTO;
import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseTaken;
import com.example.demo.models.Lesson;
import com.example.demo.models.ProgressTest;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IExerciseTakenRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IScoreService;

@Service
public class ScoreServiceImpl implements IScoreService {
	Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

	private final String DELETED_STATUS = "DELETED";
	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IExerciseTakenRepository iExerciseTakenRepository;

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private ModelMapper modelMapper;

	// unitName: process

	// subjectId --> all unit + all progressTest
	// unit --> all lesson --> all exercise
	// progressTest --> lesson = 0 --> all exercise

	@Override
	public List<ScoreResponseDTO> findAllExerciseScoreBySubjectId(long subjectId, long accountId) {
		List<ScoreResponseDTO> scoreResponseDTOList = new ArrayList<>();
		// find all unit and progressTest
		List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subjectId);
		List<ProgressTest> progressTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(subjectId);

		// order unit and progressTest
		Map<String, Long> orderMap = new LinkedHashMap<>();
		if (!unitList.isEmpty()) {
			for (Unit unit : unitList) {
				orderMap.put(String.valueOf(unit.getUnitName()), unit.getId());
				if (!progressTestList.isEmpty()) {
					for (ProgressTest progressTest : progressTestList) {
						if (unit.getId() == progressTest.getUnitAfterId()) {
							orderMap.put(progressTest.getProgressTestName(), progressTest.getId());
						}
					}
				}
			}
		}

		for (Entry<String, Long> entry : orderMap.entrySet()) {
			List<LessonScoreViewDTO> lessonScoreViewDTOList = new ArrayList<>();

			String unitName = entry.getKey();
			String process = "0";
			int totalExericse = 0;
			int countNotDone = 0;

			if (unitName.contains("Review") || unitName.contains("Semester")) {
				List<Exercise> exerciseList = new ArrayList<>();
				long progressTestId = entry.getValue();
				// get all exercise by progress id
				exerciseList
						.addAll(iExerciseRepository.findByProgressTestIdAndStatusNot(progressTestId, DELETED_STATUS));
				totalExericse = 0 + exerciseList.size();
				List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
				for (Exercise exercise : exerciseList) {
					ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise, ExerciseResponseDTO.class);
					List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
							.findByExerciseIdAndAccountId(exercise.getId(), accountId);
					if (!exerciseTakenList.isEmpty()) {
						exerciseResponseDTO.setDone(true);
					} else {
						countNotDone++;
					}
					exerciseResponseDTOList.add(exerciseResponseDTO);
				}
				LessonScoreViewDTO lessonScoreViewDTO = new LessonScoreViewDTO(0, exerciseResponseDTOList);
				lessonScoreViewDTOList.add(lessonScoreViewDTO);

			} else {
				List<Lesson> lessonList = new ArrayList<>();
				long unitId = entry.getValue();
				lessonList.addAll(iLessonRepository.findByUnitIdAndIsDisableFalse(unitId));
				if (!lessonList.isEmpty()) {
					for (Lesson lesson : lessonList) {
						int lessonName = lesson.getLessonName();
						// find all exercise of lesson
						List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
						List<Exercise> exerciseList = new ArrayList<>();
						long lessonId = lesson.getId();
						exerciseList.addAll(iExerciseRepository.findByLessonIdAndStatusNot(lessonId, DELETED_STATUS));
						totalExericse += exerciseList.size();

						if (!exerciseList.isEmpty()) {
							for (Exercise exercise : exerciseList) {
								ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise,
										ExerciseResponseDTO.class);
								// check exercise isTaken
								List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
										.findByExerciseIdAndAccountId(exercise.getId(), accountId);
								if (!exerciseTakenList.isEmpty()) {
									exerciseResponseDTO.setDone(true);
								} else {
									countNotDone++;
								}
								exerciseResponseDTOList.add(exerciseResponseDTO);
							}
						}

						LessonScoreViewDTO lessonScoreViewDTO = new LessonScoreViewDTO(lessonName,
								exerciseResponseDTOList);
						lessonScoreViewDTOList.add(lessonScoreViewDTO);
					}
				}
			}
			if (totalExericse > 0) {
				double percentDone = (totalExericse - countNotDone) * 100 / totalExericse;
				process = (int) Math.round(percentDone) + "%";
			}

			ScoreResponseDTO scoreResponseDTO = new ScoreResponseDTO();
			scoreResponseDTO.setUnitName(unitName);
			scoreResponseDTO.setProcess(process);
			scoreResponseDTO.setLessonScoreViewDTOList(lessonScoreViewDTOList);
			scoreResponseDTOList.add(scoreResponseDTO);
		}

		return scoreResponseDTOList;
	}

}
