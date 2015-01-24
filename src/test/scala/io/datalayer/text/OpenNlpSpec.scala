/****************************************************************
 * Licensed to Datalayer (http://datalayer.io) under one or     *
 * more contributor license agreements.  See the NOTICE file    *
 * distributed with this work for additional information        *
 * regarding copyright ownership. Datalayer licenses this file  *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package io.datalayer.text

import java.io.FileInputStream

import org.scalatest.Finders
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import opennlp.tools.sentdetect.SentenceDetectorME
import opennlp.tools.sentdetect.SentenceModel

class OpenNlpSpec extends FlatSpec with Matchers {

  it should "" in {
    val paragraph = "Hi. How are you? This is Mike."
    val s = sentences(paragraph)
    s.length should be(2)
  }

  def sentences(sentence: String): Array[String] = {
    val is = new FileInputStream("./src/test/resources/io/datalayer/opennlp/en-sent.bin")
    val model = new SentenceModel(is)
    val sentencedetector = new SentenceDetectorME(model)
    val sentences = sentencedetector.sentDetect(sentence)
    println(sentences.take(0))
    println(sentences.take(1))
    is.close();
    sentences;
  }

}
