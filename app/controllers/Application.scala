package controllers

import play.api._
import play.api.mvc._
import javax.xml.parsers._
import java.io._
import org.apache.poi.hwpf._
import javax.xml.transform.dom._
import javax.xml.transform.stream._
import javax.xml.transform._
import org.w3c.dom.html._
import scala.collection.JavaConversions._
import java.io.OutputStream._
import javax.imageio.ImageIO
import javax.imageio.stream._

import scala.xml.Text
object Application extends Controller {

  def index = Action {
    val pattern = "<meta.*?>|<META.*?>"
    var html2 = (pattern r) replaceFirstIn (getHtml, "")
    html2 = (pattern r) replaceFirstIn (html2, "")
    val html = scala.xml.XML.loadString(html2)
    getWordDocument()
    Ok(views.html.index(html))
  }

  def getWordDocument() = {
    val wordDocument = new org.apache.poi.hwpf.HWPFDocument(new java.io.FileInputStream("/home/ubuntuubuntu/Desktop/mswordtest/wordtest.doc"))
    val pics = wordDocument.getPicturesTable().getAllPictures()
    asList(pics).foreach {
      pic =>
        val mimeType = pic.getMimeType().toString().replace("image/","")
        val fileName = "/home/ubuntuubuntu/Desktop/mswordtest/" + pic.suggestFullFileName()
        val fo = new FileOutputStream(fileName);
        val byte = pic.getContent()
        fo.write(byte)
    }
  }

  def getHtml = {
    val wordDocument = new org.apache.poi.hwpf.HWPFDocument(new java.io.FileInputStream("/home/ubuntuubuntu/Desktop/mswordtest/wordtest.doc"))
    val wordToHtmlConverter = new org.apache.poi.hwpf.converter.WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())
    wordToHtmlConverter.processDocument(wordDocument);
    val htmlDocument = wordToHtmlConverter.getDocument();
    val out = new ByteArrayOutputStream();
    val domSource = new DOMSource(htmlDocument);
    val streamResult = new StreamResult(out);
    val tf = TransformerFactory.newInstance();
    val serializer = tf.newTransformer();
    serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
    serializer.setOutputProperty(OutputKeys.METHOD, "html");
    serializer.transform(domSource, streamResult);
    out.close();
    val result = new String(out.toByteArray());
    result.toString()
  }
}