/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.core.common.utils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
public class ValidationCodeUtil {
	
        // The width of the image.  
        private int width = 200;  
        // The height of the image.  
        private int height = 40;  
        // Number of verification code characters
        private int codeCount = 5;  
        // Verification code interference line number 
        private int lineCount = 150;  
        // Verification code
        private String code = null;  
        // Verification code Picture Buffer
        private BufferedImage buffImg=null;  
        
        private String randomCode ="";
        private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',  
                'K', 'L', 'M', 'N',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
                'X', 'Y', 'Z',  '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
      
        public  ValidationCodeUtil() {  
            this.createCode();  
        }  
      
        /** 
         *  
         * @param width Picture width
         * @param height Picture height
         */  
        public  ValidationCodeUtil(int width,int height) {  
            this.width=width;  
            this.height=height;  
            this.createCode();  
        }  
        /** 
         *  
         * @param width Picture width
         * @param height Picture height
         * @param codeCount Number of verification code characters
         * @param lineCount Verification code interference line number
         */  
        public  ValidationCodeUtil(int width,int height,int codeCount,int lineCount) {  
            this.width=width;  
            this.height=height;  
            this.codeCount=codeCount;  
            this.lineCount=lineCount;  
            this.createCode();  
        }  
          
        public void createCode() {  
            int x = 0,fontHeight=0,codeY=0;  
            int red = 0, green = 0, blue = 0;  
              
            x = width / (codeCount +2);// The width of each character  
            fontHeight = height - 2;// Height of the font
            codeY = height - 4;  
              
            buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
            Graphics2D g = buffImg.createGraphics();  
            // Generate random numbers  
            Random random = new Random();  
            // Fill the image with white
            g.setColor(Color.WHITE);  
            g.fillRect(0, 0, width, height);  
            // Create font
            ImgFontByte imgFont=new ImgFontByte();  
            Font font =imgFont.getFont(fontHeight);  
            g.setFont(font);  
              
            for (int i = 0; i < lineCount; i++) {  
                int xs = random.nextInt(width);  
                int ys = random.nextInt(height);  
                int xe = xs+random.nextInt(width/8);  
                int ye = ys+random.nextInt(height/8);  
                red = random.nextInt(255);  
                green = random.nextInt(255);  
                blue = random.nextInt(255);  
                g.setColor(new Color(red, green, blue));  
                g.drawLine(xs, ys, xe, ye);  
            }  
              
            // randomCode records randomly generated verification code  
            StringBuffer randomCode = new StringBuffer();  
            // Randomly generate a verification code for codeCount characters.  
            for (int i = 0; i < codeCount; i++) {  
                String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);  
                // Generate random color values ​​so that the color value of each character output will be different. 
                red = random.nextInt(255);  
                green = random.nextInt(255);  
                blue = random.nextInt(255);  
                g.setColor(new Color(red, green, blue));  
                g.drawString(strRand, (i + 1) * x, codeY);  
                // Combine the four random numbers generated.  
                randomCode.append(strRand);  
            }  
            // Save the four-digit verification code to the Session. 
            code=randomCode.toString();       
        }  
        

          
        public void write(String path) throws IOException {  
            OutputStream sos = new FileOutputStream(path);  
                this.write(sos);  
        }  
          
        public void write(OutputStream sos) throws IOException {  
                ImageIO.write(buffImg, "png", sos);  
                sos.close();  
        }  
        public BufferedImage getBuffImg() {  
            return buffImg;  
        }  
          
        public String getCode() {  
            return code;  
        }  
        
        /** 
         * @param args 
         */  
        public static void main(String[] args) {  
        	ValidationCodeUtil vCode = new ValidationCodeUtil(180,40,6,100);  
            try {  
                String path="D:/"+new Date().getTime()+".png";  
                System.out.println(vCode.getCode()+" >"+path);  
                vCode.write(path);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
}

    
     class ImgFontByte {  
        public Font getFont(int fontHeight){  
            try {  
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(hex2byte(getFontByteStr())));  
                return baseFont.deriveFont(Font.PLAIN, fontHeight);  
            } catch (Exception e) {  
                return new Font("Arial",Font.PLAIN, fontHeight);  
            }  
        }  
          
        private  byte[] hex2byte(String str) {   
            if (str == null)  
                return null;  
            str = str.trim();  
            int len = str.length();  
            if (len == 0 || len % 2 == 1)  
                return null;  
      
            byte[] b = new byte[len / 2];  
            try {  
                for (int i = 0; i < str.length(); i += 2) {  
                    b[i / 2] = (byte) Integer  
                            .decode("0x" + str.substring(i, i + 2)).intValue();  
                }  
                return b;  
            } catch (Exception e) {  
                return null;  
            }  
        } 
     /** 
      * The hex string of the ttf font file
      * @return 
      */  
     private String getFontByteStr(){ 
	    	 return null;  
	    
	    }  
    }  
