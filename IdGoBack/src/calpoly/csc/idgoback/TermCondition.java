package calpoly.csc.idgoback;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TermCondition extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_and_condition);
        
        TextView tv = (TextView) findViewById(R.id.textView3);
        String accept_term = "You affirm that you are either more than 18 years of age, or an emancipated minor, or possess legal parental or guardian consent, and are fully able and " +
        					"competent to enter into the terms, conditions, obligations, affirmations, representations, and warranties set forth in these Terms of Service (\"Terms\"), " +
        					"and to abide by and comply with these Terms, created by and posted on the Idgoback.com website (\"Site\" or \"I'd Go Back ®\"). " + 
        					"You will hereinafter be referred to as \"User\". Please expressly note that if you are under the age of 13, you are prohibited " + 
        					"from using the I'd Go Back media, LLC website.\n\n";
        accept_term += "These Terms of Service apply to all Users of the I'd Go Back media, LLC website, including Users who are also contributors of photographic or video content, information, " + 
        				"and other materials or services on the Website. The I'd Go Back ® Website includes all aspects of idgoback.com, including but not limited to all content, products, software "+ 
        				"and services offered via the website and the applications contained therein.\n";
        tv.setText(accept_term);
        
        tv = (TextView) findViewById(R.id.textView5);
        String agreement = "This website is operated by Idgoback.com (“I'd Go Back ®”). The Site consists of a mix of digital media and written content provided by I'd Go Back ®, our Users and our partners " + 
        					"(together referred to herein as \"Content\"). You are responsible for all Content that you post on, or transmit through, the Site. For purposes of these Terms of Service, \"Content\" "+ 
        					"means any text, data compilations, video uploads, photographs, software, data instruction package, graphic, illustration, artwork, video, music, sound, and/or any other content found or "+ 
        					"posted onto I'd Go Back ®.\n\n";
        agreement +="The following are the Terms of Service by which you agree to be bound when using this Site. I'd Go Back media, LLC reserves the right to modify these Terms at any time. You will be responsible for " + 
        			"reviewing and complying with these Terms of Service periodically in the event that they are modified. By agreeing to these Terms and using the website and its services you also consent to the collection, " + 
        			"use and disclosure of information as set forth in the I'd Go Back ® Privacy Policy, posted on this website and updated from time to time. You will be responsible to review the Site’s Privacy Policy periodically.\n";
        tv.setText(agreement);
        
        tv = (TextView) findViewById(R.id.textView7);
        String use_content="I'd Go Back ® grants you a limited, revocable, non-exclusive license (i) to access idgoback.com, (ii) to view Content displayed on I'd Go Back ® for your personal or internal business use, (iii) to print a copy " +
        					"of any portion of the Content of I'd Go Back ® for your personal use, provided it is no more than ten (10) pages in sequence, (iv) to stream video content from the site. \"Streaming\" means a contemporaneous digital "+ 
        					"transmission of an audiovisual work from the Internet to a User's device in a manner intended for real-time viewing. It is not to be copied, stored, permanently downloaded, or redistributed by the user in any form. " + 
        					"User Videos are made available exactly as they are uploaded.\n\n";
        use_content +="You shall use the Content in strict compliance with all applicable Federal, state and local laws, rules and regulations, including but not limited to those concerning privacy, phone solicitation, fax and/or e-mail transmissions, " + 
        				"direct marketing, and any third party’s patent, copyright or other intellectual property rights and interests.\n\n";
        use_content += "You understand that when using the I'd Go Back ® website, you will be exposed to User Submissions from a variety of sources, and that I'd Go Back ® is not responsible for the accuracy, usefulness, safety, or intellectual property "+
        				"rights of, or relating to, such User Submissions. You further understand and acknowledge that you may be exposed to User Submissions that are inaccurate, offensive, indecent, or objectionable to you, and you agree to waive, and hereby "+
        				"do waive, any legal or equitable rights or remedies you have or may have against I'd Go Back ® as related to all matters attached to or derived from your use of the Site, and agree to indemnify and hold harmless I'd Go Back ®, its owners/operators, "+
        				"affiliates, and/or licensors, to the fullest extent allowed by law.\n\n";
        use_content += "I'd Go Back ® MAY CONTAIN LINKS TO THIRD PARTY WEBSITES. SUCH LINKS ARE PROVIDED SOLELY AS A CONVENIENCE TO THE USER. I'd Go Back ® IS NOT RESPONSIBLE FOR THE CONTENT OF LINKED THIRD-PARTY SITES. I'd Go Back ® DOES NOT ENDORSE, SUPPORT, OR MAKE ANY "+ 
        				"REPRESENTATIONS REGARDING THE ACCURACY, MARKETABILITY, VERACITY OR APPROPRIATENESS OF CONTENT ON SUCH THIRD-PARTY WEB SITES. ANY ACCESS TO THIRD-PARTY SITES FROM LINKS ON I'd Go Back ® IS DONE AT THE SOLE RISK OF THE USER.\n";
        tv.setText(use_content);
        
        tv = (TextView) findViewById(R.id.textView9);
        String condSub = "As an I'd Go Back ® account holder you may submit photographic content (\"User Photos\") video content (\"User Videos\") and textual content (\"User Comments\"), including but not limited to commentary and personal opinion. User Photos, Videos and Comments "  +
        				  "are collectively referred to as \"User Submissions\" or \"Submissions\".\n\n";
        condSub += "You understand I'd Go Back ® does not guarantee any confidentiality with respect to any User Submissions. You shall be solely responsible for your own User Submissions and the consequences of posting or publishing them.\n\n";
        condSub += "In connection with User Submissions, you affirm, represent, and/or warrant that you own or have the licenses, rights, consents, and permissions necessary to use the Submissions and by posting such Content automatically authorize inclusion and use of the Submissions "+
        			"in the manner contemplated by the Site and these Terms of Service. I'd Go Back ® does not endorse any User Submission or any opinion, recommendation, or advice expressed therein, and expressly disclaims any and all liability in connection with all User Submissions.\n\n";
        condSub += "I'd Go Back ® does not permit activities that infringe any intellectual property (“IP”) right including but not limited to copyright, trademark, trade dress, patent or other IP infringing activities on its Website, and will remove all Content and User Submissions if "+ 
        			"properly notified that such Content or Submission infringes on another's IP rights. I'd Go Back ® reserves the right to remove Content and Submissions without prior notice, at its sole discretion.\n\n";
        condSub += "I'd Go Back ® reserves the right to decide whether any Content or Submission is appropriate and complies with these Terms of Service for violations other than copyright infringement; such as, but not limited to, pornography, obscene or defamatory material, or excessive length. " +
        			"I'd Go Back ® may remove such User Submissions and/or terminate a User's access to the website or for uploading such material in violation of these Terms of Service, at any time without prior notice and at its sole discretion.\n";
        tv.setText(condSub);
        
        tv = (TextView) findViewById(R.id.textView11);
        String post_transmit_content ="The following Content is prohibited:\n\n";
        post_transmit_content +="1. Copyrighted material and trademarks that are used without the express permission of the owner;\n";
        post_transmit_content +="2. Advertisements, promotions, solicitations or offers to sell any goods or services for any commercial purpose unless you have the written consent of I'd Go Back ® to do so;\n";
        post_transmit_content +="3. Pornographic, sexually explicit or obscene Content;\nContent that exploits children or minors;\n";
        post_transmit_content +="4. Content that discloses or contains any personally identifying information beyond a first name about any person who appears to be under the age of 18 or that includes private, confidential information;\n";
        post_transmit_content +="5. Content that we have reason to believe was posted for malevolent purposes, including but not limited to: libel, slander, defamation or harassment;\n";
        post_transmit_content +="6. Content that may be deemed grossly offensive to a reasonable User of the Site, including but not limited to: blatant expressions of bigotry, prejudice, racism, hatred or profanity directed generally or specifically at any individual, group or any portion of the population;\n";
        post_transmit_content +="7. Content promoting or providing instructional information about illegal or illicit activities;\n";
        post_transmit_content +="8. Content that transmits or contains viruses, corrupted files, or any similar element.\n\n";
        post_transmit_content +="9. We examine the Content of the Site on a regular basis and maintain the absolute right to remove any Content that does not meet the standards listed above, or that is otherwise objectionable, at our sole discretion. We are not responsible for the loss, deletion, failure to store, "+
        						"untimely or mis-delivery of any Content submitted or transmitted through, the Site.\n";
        tv.setText(post_transmit_content);
        
        tv = (TextView) findViewById(R.id.textView13);
        String license ="When you post or transmit Content on or through the Site you grant I'd Go Back ®, their owners, operators, affiliates and partners a nonexclusive, perpetual, irrevocable, worldwide, sub-licensable, royalty-free license to use, store, display, publish, transmit, transfer, distribute, reproduce, "+
        				"rearrange, edit and modify your uploaded Content for any purpose on the Site; or through each of the services provided by this or other sites owned or co-managed by I'd Go Back ® , its affiliates or partners.\n\n";
        license +="This license shall apply to the distribution and the storage of your Content in any form, medium, or technology now known or later developed. We retain the right to use your name, city and state in connection with any use of your Content.\n";
        tv.setText(license);
        
        tv = (TextView) findViewById(R.id.textView15);
        String indemnity ="You agree to defend, indemnify and hold harmless I'd Go Back ®, its officers, directors, employees and agents, from and against any and all claims, damages, obligations, losses, liabilities, costs or debt, and expenses (including but not limited to attorney's fees) arising from: (i) your access "+
        					"to and use of the website; (ii) your violation of any portion of these Terms of Service; (iii) your violation of any third party right, including but not limited to any full or partial copyright, property, or privacy right; or (iv) any claim that one of your User Submissions caused damage to a third party. "+
        					"This defense and indemnification obligation will survive these Terms of Service and your use of the I'd Go Back ® Website.\n";
        tv.setText(indemnity);
        
        tv = (TextView) findViewById(R.id.textView17);
        String legal ="1. Dispute Resolution: The Terms of Service shall be governed by and construed in accordance with the internal laws of the State of California (CA) without regard to the conflicts of laws principles thereof. In the event of litigation, the parties agree to submit irrevocably to the state and federal courts located in LA "+
        				"County and to raise no objections to the venue of such courts. The parties agree that in the event of any complaint or proceeding brought by one party against the other, the party prevailing shall be entitled to the collection of reasonable attorney’s fees from the other party.\n";
        legal +="2. Severability: If any provision of the Terms of Service is found to be invalid by any court or tribunal having competent jurisdiction, the invalidity of such provision shall not affect the validity of the remaining provisions of the Terms of Service, which shall remain in full force and effect.\n";
        legal +="3. Waiver: No waiver of any provision of these Terms of Service shall be deemed a further or continuing waiver of any other term.\n";
        legal +="4. Assignment: I'd Go Back ® may assign its rights and duties under these Terms of Service to any party at any time without notice to the User. User's rights and duties under the Terms of Service are not assignable without consent of I'd Go Back media, LLC.\n";
        legal +="5. Termination: I'd Go Back ® may terminate User's access, or rights of use, to any isolated portion or all portions of the website and/or its Content for any reason. The provisions carried in these Terms of Service shall survive the termination of this and any affiliate website.\n";
        legal +="6. YOU AND I'd Go Back ® AGREE THAT ANY CAUSE OF ACTION ARISING OUT OF OR RELATED TO USE OF THE SITE MUST COMMENCE WITHIN ONE (1) YEAR AFTER THE CAUSE OF ACTION ARRISES. OTHERWISE, SUCH CAUSE OF ACTION SHALL BE PERMANENTLY BARRED.\n";  		
        tv.setText(legal);
        
	}
}
