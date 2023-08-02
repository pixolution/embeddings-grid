package de.pixolution.embeddingsGrid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import de.pixolution.som.data.EmbeddingData;
import de.pixolution.som.data.Grid;

public class GridHtmlView {
	
	private final int imgWidth = 150;
	private final int imgHeight = 150;

	public GridHtmlView(Grid sortedGrid, Map<String, String> uidToFilePathMap, String outputPath, String uriPrefix) {
		System.out.println("Got array with length "+sortedGrid.getElementCount());
		StringBuilder bodyHtml = new StringBuilder();
		int counter=0;
		bodyHtml.append("<table>\n");
		for (int row=0; row<sortedGrid.getRows(); row+=1) {
			bodyHtml.append("  <tr>\n");
			for (int column=0; column<sortedGrid.getColumns(); column+=1) {
				bodyHtml.append("    <td>\n");
				EmbeddingData data = sortedGrid.getElement(column, row);
				if (data != null) {
					String fileUri = null;
					if (uriPrefix==null) {
						fileUri = uidToFilePathMap.get(data.getId());
					} else {
						fileUri = "file://"+uidToFilePathMap.get(data.getId());
					}
					counter+=1;
					String extraStyle = "";
					if (data.isLargerThanOneSlot()) {
						data.toString();
						// this element is oversized, mark with color border
						extraStyle = "border:10px solid red;";
					}
					bodyHtml.append("<a href=\""+fileUri+"\"><img style=\"width: "+imgWidth+"px; height: "+imgHeight+"px; object-fit: cover;"+extraStyle+"\" src=\""+fileUri+"\" /></a>\n");
				}
				bodyHtml.append("    </td>\n");
			}
			bodyHtml.append("  </tr>\n");
		}
		bodyHtml.append("</table>\n");
		System.out.println("Placed "+counter+" images in HTML table");
		try {
			storeAsFile(generateHtml("", "<h1>Arrangement of "+counter+" images on "+sortedGrid.getColumns()+"x"+sortedGrid.getRows()+" grid</h1>\n"
					+ "<p>Made by <a href=\"https://pixolution.org\">pixolution GmbH, Berlin</a>.<small> We <span style=\"color:#C90A30\">♥</span> to build AI.</p>\n" + bodyHtml.toString()), outputPath);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to write file "+outputPath+"\n"+generateHtml("", bodyHtml.toString()));
		}
	}
	
	private void storeAsFile(String content, String path) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(path));
		try {
			System.out.println("Wrote HTML preview to file://"+path);
		    out.write(content);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    out.close();
		}
	}
	
	private String generateHtml(String css, String body) {
		return new String("<!DOCTYPE html>\n"
						+ "<html>\n"
						+ "<head>\n"
						+ "   <style>\n"
//						+ "      div {\n"
//						+ "        border: 2px solid #CCCCCC;\n"
//						+ "        width: 300px;\n"
//						+ "        height: 300px;\n"
//						+ "      }\n"
				        + "   </style>\n"
						+ "</head>\n"
						+ "<body>\n"
						+    body
						+ "</body>\n"
						+ "</html>\n");
	}
	
}
