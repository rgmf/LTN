/**
 * Copyright (C) 2014 Román Ginés Martínez Ferrández <rgmf@riseup.net>
 *
 * This program (LibreTeacherNotebook) is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU General 
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.rgmf.ltn.fragments;

import java.io.File;
import java.io.IOException;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import es.rgmf.ltn.R;
import es.rgmf.ltn.model.WriterModel;
import es.rgmf.ltn.model.orm.CheckFieldException;
import es.rgmf.ltn.model.orm.Student;
import es.rgmf.ltn.util.Utilities;

/**
 * This class is a fragment that offer the user to create a student in
 * a course.
 * 
 * @author Román Ginés Martínez Ferrández.
 */
public class UpdateCourseStudentFragment extends Fragment {
    private static final int SELECT_PICTURE = 1;
    
    /**
     * The application context.
     */
    private Context mContext;
    /**
     * The student object.
     */
    private Student mStudent;
    /**
     * The course identify.
     */
    private int mCourseId;
    /**
     * The root view.
     */
    private View mRootView;
    /**
     * The path of the photo.
     */
    private String mPhoto = "";
	
	/**
	 * Create the fragment instance.
	 * 
	 * @param student The student.
	 * @return
	 */
	public static UpdateCourseStudentFragment newInstance(Student student, int courseId) {
		UpdateCourseStudentFragment fragment = new UpdateCourseStudentFragment();
		fragment.mStudent = student;
		fragment.mCourseId = courseId;
		fragment.mPhoto = student.getPhoto();
		return fragment;
	}
	
	/**
	 * Method that is called when the view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_add_student, container, false);
		
		if(getActivity() != null) {
			mContext = getActivity().getApplicationContext();
		}
		
		// Load data in inputs.
        Bitmap photoBitmap = Utilities.loadBitmapEfficiently(mStudent.getPhoto(),  
    			(int) getResources().getDimension(R.dimen.icon_size_medium),
    			(int) getResources().getDimension(R.dimen.icon_size_large));
		((ImageView) mRootView.findViewById(R.id.student_photo_input)).setImageBitmap(photoBitmap);
		((EditText) mRootView.findViewById(R.id.student_name_input)).setText(mStudent.getName());
		((EditText) mRootView.findViewById(R.id.student_lastname_input)).setText(mStudent.getLastname());		
		// When user click on photo button it executes this method.
		mRootView.findViewById(R.id.student_photo_input).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
			}
		});
		
		// When user click on add student button it executes this method.
        mRootView.findViewById(R.id.add_student_button).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				try {
					mStudent.setName(((EditText) mRootView.findViewById(R.id.student_name_input)).getText().toString());
					mStudent.setLastname(((EditText) mRootView.findViewById(R.id.student_lastname_input)).getText().toString());
					mStudent.setPhoto(mPhoto);
					WriterModel.updateStudent(mContext, mStudent);
				} catch(Exception e) {
					Log.v("UpdateCourseStudentFragment", "onCreateView::" + e.getMessage());
					Toast.makeText(getActivity(), getString(R.string.error_updating_student) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
				
				// Load the Course Student List Fragment.
				Fragment fragment1 = CourseStudentListFragment.newInstance(mCourseId);
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.course_student_list_fragment, fragment1);
				ft.commit();
				
				// Load the Course Student Detail Fragment.
				Fragment fragment2 = new CourseStudentDetailFragment();
				ft = fm.beginTransaction();
				ft.replace(R.id.course_student_detail_fragment, fragment2);
				ft.commit();
				
				// Hide soft keyboard.
				Utilities.hideSoftKeyboard(getActivity());
			}
        });
        
        // When user click on cancel student button it executes this method.
        mRootView.findViewById(R.id.cancel_student_button).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// Load the Course Student List Fragment.
				Fragment fragment1 = CourseStudentListFragment.newInstance(mCourseId);
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.course_student_list_fragment, fragment1);
				ft.commit();
				
				// Load the Course Student Detail Fragment.
				Fragment fragment2 = new CourseStudentDetailFragment();
				ft = fm.beginTransaction();
				ft.replace(R.id.course_student_detail_fragment, fragment2);
				ft.commit();
				
				// Hide soft keyboard.
				Utilities.hideSoftKeyboard(getActivity());
			}
        });
		
		return mRootView;
	}
	
	/**
	 * Set the image that user selected.
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                try {
                	Bitmap photoBitmap = Utilities.loadBitmapEfficiently(selectedImagePath,  
                			(int) getResources().getDimension(R.dimen.icon_size_medium),
                			(int) getResources().getDimension(R.dimen.icon_size_large));
                	
					String fileCopied= Utilities.copyFileUsingFileChannels(
								new File(selectedImagePath),
								new File(getActivity().getApplicationInfo().dataDir));
					mPhoto = fileCopied;
					//((ImageView) mRootView.findViewById(R.id.student_photo_input)).setImageURI(selectedImageUri);
					((ImageView) mRootView.findViewById(R.id.student_photo_input)).setImageBitmap(photoBitmap);
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), getString(R.string.error_loading_image) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
            }
        }
    }
 
	/**
	 * Get the path of the uri.
	 * 
	 * @param uri
	 * @return
	 */
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
