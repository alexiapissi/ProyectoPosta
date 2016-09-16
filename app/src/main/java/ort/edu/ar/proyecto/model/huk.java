package ort.edu.ar.proyecto.model;

/**
 * Created by 41824471 on 16/9/2016.
 */
public  class huk{/* extends AsyncTask<QuestionParam, Void, Question> {
        private final ProgressDialog dialog = new ProgressDialog(ctx);
        private OkHttpClient client = new OkHttpClient();
        private String errorMsg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Uploading question...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Question question) {
            super.onPostExecute(question);
            dialog.dismiss();
            if (_questionListener !=null) {
                ArrayList<Question> questions = new ArrayList<>();
                questions.add(question);
                _questionListener.questionServiceComplete(questions,errorMsg, serviceRequested); // Callback
            }
        }

        @Override
        protected Question doInBackground(QuestionParam... params) {

            try {
                QuestionParam qp = params[0];
                String question=qp.question;
                String questionURL=qp.questionURL;
                ArrayList<Answer> answers=qp.answers;
                int category=qp.category.getPk();
                Uri imgUri = qp.imageUri;

                MultipartBuilder mpb = new MultipartBuilder()
                        .type(MultipartBuilder.FORM);

                addMultipartField(mpb,question,"question");
                addMultipartField(mpb,questionURL,"url");

                int i=0;
                for (Answer a:answers) {
                    i++;
                    addMultipartField(mpb, a.getAnswer(), "answer" + i);
                }
                addMultipartField(mpb,String.valueOf(category),"category");

                if (imgUri != null && !imgUri.toString().isEmpty()) {
                    Bitmap finalImage;
                    if (imgUri.getScheme().startsWith("http")) {
                        finalImage=getBitmapFromURL(imgUri.toString());
                    }
                    else {
                        // Get Bitmap image from Uri
                        ParcelFileDescriptor parcelFileDescriptor =
                                ctx.getContentResolver().openFileDescriptor(imgUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap originalImage = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();

                        // Rotate image if needed according to EXIF orientation
                        InputStream is = ctx.getContentResolver().openInputStream(imgUri);
                        ImageHeaderParser ihp = new ImageHeaderParser(is);
                        int orientation = ihp.getOrientation();
                        Bitmap rotatedImg = ImageUtils.getConstrainedBitmap(originalImage, MAX_IMG_SIZE);// Resize image if higher than MAX size
                        finalImage = ImageUtils.rotateBitmap(rotatedImg, orientation);
                    }
                    // Convert bitmap to output string
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);   // Compress to PNG lossless
                    byte[] byteArray = stream.toByteArray();

                    String fileName = UUID.randomUUID().toString() + ".png";
                    mpb.addPart(Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"" + fileName + "\""),
                            RequestBody.create(MEDIA_TYPE_PNG, byteArray));
                }

                String url=urlApi+"questions/create";
                RequestBody requestBody =mpb.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    errorMsg = response.body().string();
                    Log.i("QuestionService", "Question create error:" + errorMsg);
                    return null;
                }
                else {
                    String JSONResp = response.body().string();
                    JSONObject JSONQuestion = new JSONObject(JSONResp);
                    return convertQuestion(JSONQuestion);
                }

            }
            catch (java.io.IOException |ArrayIndexOutOfBoundsException| JSONException e) {
                e.printStackTrace();
                errorMsg=e.getMessage();
                return null;
            }
        }

        private void addMultipartField( MultipartBuilder mpb, String value, String fieldname){
            if (!value.isEmpty())
                mpb.addPart(Headers.of("Content-Disposition", "form-data; name=\""+fieldname+"\""),
                        RequestBody.create(null, value));

        }

        private Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }
    }*/
}
