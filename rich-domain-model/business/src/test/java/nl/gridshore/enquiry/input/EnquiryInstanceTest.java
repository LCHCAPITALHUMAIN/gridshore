/*
 * Copyright (c) 2009. Gridshore
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.gridshore.enquiry.input;

import nl.gridshore.enquiry.def.EnquiryDef;
import nl.gridshore.enquiry.def.OpenQuestionDef;
import nl.gridshore.enquiry.def.QuestionDef;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EnquiryInstanceTest {

    private EnquiryInstance testSubject;
    private EnquiryDef enquiryDef;
    private OpenQuestionDef question1;
    private OpenQuestionDef question2;

    @Before
    public void setUp() {
        List<QuestionDef> questions = new ArrayList<QuestionDef>();
        question1 = new OpenQuestionDef("This is question 1");
        question2 = new OpenQuestionDef("This is question 2");
        questions.add(question1);
        questions.add(question2);
        enquiryDef = new EnquiryDef("Test definition", questions);
        testSubject = new EnquiryInstance(enquiryDef);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testSubject.getAnswers().size());
        assertSame(enquiryDef, testSubject.getEnquiryDef());
    }

    @Test
    public void testAddAndRemoveQuestions() {
        assertEquals(0, testSubject.getAnswers().size());
        testSubject.addAnswer(new OpenAnswerInstance(question1, "Some answer"));
        assertEquals(1, testSubject.getAnswers().size());
        testSubject.addAnswer(new OpenAnswerInstance(question1, "Revised answer"));
        assertEquals(1, testSubject.getAnswers().size());
        try {
            testSubject.addAnswer(new OpenAnswerInstance(new OpenQuestionDef(""), "Wrong question"));
            fail("Expected an IllegalArgumentException");
        }
        catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("other enquiry"));
        }
        assertEquals(1, testSubject.getAnswers().size());
        assertEquals("Revised answer", testSubject.getAnswerForQuestion(question1).getAsText());
        testSubject.removeAnswerForQuestion(question1);
        assertEquals(0, testSubject.getAnswers().size());
    }

    @Test
    public void testBuildAnswerMap() throws NoSuchFieldException, IllegalAccessException {
        testSubject = new EnquiryInstance();
        assertNull(testSubject.getAnswerForQuestion(question1));
        Field answerInstancesField = testSubject.getClass().getDeclaredField("answerInstances");
        answerInstancesField.setAccessible(true);
        List<AnswerInstance> questions = new ArrayList<AnswerInstance>();
        questions.add(new OpenAnswerInstance(question1, "q1"));
        questions.add(new OpenAnswerInstance(question2, "q2"));
        answerInstancesField.set(testSubject, questions);
        assertNull(testSubject.getAnswerForQuestion(question1));

        testSubject.populateAnswerMap();

        assertNotNull(testSubject.getAnswerForQuestion(question1));
    }
}