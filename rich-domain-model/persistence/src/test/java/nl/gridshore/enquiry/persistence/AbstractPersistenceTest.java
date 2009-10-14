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

package nl.gridshore.enquiry.persistence;

import nl.gridshore.enquiry.def.ChoiceDef;
import nl.gridshore.enquiry.def.EnquiryDef;
import nl.gridshore.enquiry.def.MultipleChoiceQuestionDef;
import nl.gridshore.enquiry.def.OpenQuestionDef;
import nl.gridshore.enquiry.def.QuestionDef;
import org.springframework.test.jpa.AbstractJpaTests;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPersistenceTest extends AbstractJpaTests {
    @Override
    protected String[] getConfigLocations() {
        return new String[]{"META-INF/spring/persistence-context.xml"};
    }

    public void onSetUpInTransaction() {
        clearEnquiryDefs();
    }

    @SuppressWarnings({"unchecked"})
    private void clearEnquiryDefs() {
        List<EnquiryDef> defs = sharedEntityManager.createQuery("SELECT q FROM EnquiryDef q").getResultList();
        for (EnquiryDef def : defs) {
            sharedEntityManager.remove(def);
        }
        sharedEntityManager.flush();
    }

    /**
     * Creates an enquiry instance with an open question and a multiple choice question with two options. The second
     * option has an open sub question.
     *
     * @return a simple Enquiry structure for test purposes
     */
    protected EnquiryDef createSimpleEnquiry() {
        List<QuestionDef> questions = new ArrayList<QuestionDef>();
        questions.add(new OpenQuestionDef("Question 1"));
        List<ChoiceDef> choices = new ArrayList<ChoiceDef>();
        choices.add(new ChoiceDef("Some text"));
        List<QuestionDef> subQuestions = new ArrayList<QuestionDef>();
        subQuestions.add(new OpenQuestionDef("Give some details"));
        choices.add(new ChoiceDef("Option requiring additional info", subQuestions));
        questions.add(new MultipleChoiceQuestionDef("Make a choice", choices));
        return new EnquiryDef("My new enquiry", questions);
    }
}