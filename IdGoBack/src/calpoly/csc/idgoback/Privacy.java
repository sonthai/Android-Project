package calpoly.csc.idgoback;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Privacy extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacy_info);
		
		TextView tv = (TextView) findViewById(R.id.text3);
		String msg = "At idgoback.com (\"I'd Go Back ¨\") we recognize that your privacy is " +
					  "important. We use commercially reasonable efforts to ensure that the collection " + 
					  "of personal information is limited to that which is necessary to fulfill the ideal " + 
					  "uses of our website, and would like to help customers clearly understand our data collection and use practices.\n\n";
		msg += "The terms \"we\", \"our\" and \"us\" refer to idgoback.com . The terms \"you\" and \"your\" refer to you, as a user of the Site. " + 
				"The term \"personal information\" means information that you provide to us which personally identifies you, such as your name, phone " + 
				"number, email address, and any other data that is tied to such information.\n\n";
		msg += "The following Consumer Privacy statement applies to all I'd Go Back media, LLC businesses. At I'd Go Back ¨, we respect the privacy of " + 
				"our users and the importance of the information they entrust to us.\n\n";
		msg += "BY USING THE SITE, YOU AGREE TO THE TERMS OF THIS PRIVACY POLICY. IF YOU DO NOT AGREE WITH THESE TERMS, YOU SHOULD NOT USE THE SITE.\n";
		tv.setText(msg);
		
		tv = (TextView) findViewById(R.id.text5);
		msg ="We may collect information that can identify you (\"personal information\"), such as your name and email address, when you (or other users) provide " +
			"it to us when using our website or in some other manner, from our business partners, and from other third parties. We may combine the personal information " + 
				"that we receive from different sources.\n\n";
		msg += "Account information. If you create an account to take advantage of the full range of services offered on the Site, we ask for and record personal information " + 
				"such as your name and email address. We use your email address to send you updates and news, and contact you on behalf of other users.\n\n" +
				"Submissions. The term \"submissions\"refers to the information that you submit or post to the Site for public display, such as business ratings, " + 
				"reviews, photos, compliments, video, etc. We record and may publicly display your submissions in order to provide the services that we offer.\n\n" +
				"We also may collect other types of information in the following ways when you visit our website:\n\n";
		
		msg += "* Our server logs automatically collect information, such as your IP address, your browser type and language, and the date and time of your visit, which helps " + "" +
				"us track users' movements around our site and understand trends.\n";
		msg += "* We may assign your computer one or more cookies which may collect information to facilitate access to our website and to personalize your online experience.\n";
		msg += "* We may use standard Internet tools, such as web beacons, which collect information that tracks your use of our website and enables us to customize our services and advertisements.\n";
		tv.setText(msg);
		
		tv = (TextView) findViewById(R.id.text7);
		msg = "* We do not knowingly collect personal information from children under the age of 13. Should we ever do so, we will comply with the Children's Online Privacy Protection Act.\n";
		msg += "* This Site may contain links to third party websites to which we have no affiliation. Except as set forth herein, we do not share your Personal Data with those third parties, " + 
			   "and are not responsible for their privacy practices. We suggest you read the privacy policies on all such third party websites.\n";
		tv.setText(msg);
		
		tv = (TextView) findViewById(R.id.text9);
		msg = "We may use the information we collect from you to:\n\n";
		msg += "* Fulfill your requests for products and services;\n" +
			   "* Offer products and services that may be of interest to you;\n" +
			   "* Customize the advertising and content that you see on our website;\n" +
			   "* Facilitate use of our website;\n" +
			   "* Manage your account and your preferences;\n" + 
			   "* Analyze use of and improve our website, products and services;\n" +
			   "* Identify and protect against fraudulent transactions and other misuses of our website; and\n" +
			   "* Enforce our Terms of Use.\n";
		tv.setText(msg);
		
		tv = (TextView) findViewById(R.id.text11);
		msg ="Occasionally, we may ask for personal information for our own internal marketing use. You may provide " + 
			 "this information on a voluntary basis. To help prevent unauthorized access or disclosure and to ensure the " + 
			 "appropriate use of the information, I'd Go Back ¨ has in place appropriate procedures to safeguard the information we collect.\n\n";
		msg+="You may choose not to provide personal information, although that may result in your inability to obtain certain services or use certain features of our website;\n";
		tv.setText(msg);
		
		tv = (TextView) findViewById(R.id.text13);
		msg = "We will make your personally identifiable information available to other companies or people when:\n\n";
		msg += "1. You have elected to allow us to share that information at the time that you registered with us or provided " + 
				"us the information, or through a subsequent affirmative election through our website.\n";
		msg += "2.You volunteer information or create a public profile in the course of your participation in our community features " + 
				"such as forums, user opinions and reviews, chat rooms, photo sharing or other forms of public communication and interaction.\n\n";
		msg += "We hire third-party vendors to provide specialized services such as customer support; email message deployment; suppression, merge and " + 
			   "de-duplication services; data processing; and special products or services that you have requested. These companies are only allowed to use " + 
				"the information in order to help us fulfill our services to you. We do not provide your information to these companies for their own, permanent use.\n\n";
		msg += "You are the winner of a contest or a sweepstakes that is co-sponsored with a third party and that third party needs your information in order to manage the prize fulfillment process.\n\n" +
				"When required by law, such as when we respond to subpoenas, court orders, or legal process.\n\n" + 
				"We believe that your actions violate applicable laws, our Terms of Use, or any usage guidelines for specific products or services, or threaten the rights, property, or safety of our company, our users, or others.\n\n";
		msg += "1. Business transfers\n2. We may buy or be acquired by another company. If we sell a business, we may transfer some or all of your information as a part of the sale in order that the service being provided to you may " + 
				"continue or for other business purposes.\n";
		tv.setText(msg);
		
		tv = (TextView) findViewById(R.id.text15);
		msg = "If you are a resident of California, in addition to the rights set forth above, you have the right to request information from us regarding the manner in which we share certain categories of personal information. " + 
			  "California law gives you the right to send us a request at a designated address info@idgoback.com to receive the following information:\n\n";
		msg += "1. the categories of information we disclosed to third parties for their direct marketing purposes during the preceding calendar year\n" +
			   "2. the names and addresses of the third parties that received that information; and\n" +
			   "3. if the nature of the third party's business cannot be determined from their name, examples of the products or services marketed.\n" +
			   "4. We may provide this information in a standardized format that is not specific to you.\n";
		tv.setText(msg);
		
		tv = (TextView) findViewById(R.id.text17);
		msg = "We may occasionally update our Privacy Policy to reflect changes in our practices and services. We may not notify you of changes in the way we collect, " + 
			  "use, or share your personal information, but we will post a notice of the changes on this page of our website. Please check this page periodically for policy changes. " + 
			  "It is the user's obligation to review our privacy statement page periodically to remain aware of any changes.\n\n";
		msg += "If you have any questions about our privacy policy or our information practices, please contact us by email at info@idgoback.com\n";
		tv.setText(msg);
		
	}
	

}
