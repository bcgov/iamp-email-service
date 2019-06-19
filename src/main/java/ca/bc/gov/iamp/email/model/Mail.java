package ca.bc.gov.iamp.email.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Mail
 */
@Validated

public class Mail   {
  @JsonProperty("subject")
  private String subject = null;

  @JsonProperty("from")
  private String from = null;

  @JsonProperty("to")
  @Valid
  private List<String> to = new ArrayList<>();

  @JsonProperty("cc")
  @Valid
  private List<String> cc = null;

  @JsonProperty("bcc")
  @Valid
  private List<String> bcc = null;

  @JsonProperty("body")
  private String body = null;

  @JsonProperty("attachments")
  @Valid
  private List<Attachment> attachments = null;

  public Mail subject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Email Subject
   * @return subject
  **/
  @ApiModelProperty(value = "Email Subject")


  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Mail from(String from) {
    this.from = from;
    return this;
  }

  /**
   * Email From
   * @return from
  **/
  @ApiModelProperty(value = "Email From")


  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public Mail to(List<String> to) {
    this.to = to;
    return this;
  }

  public Mail addToItem(String toItem) {
    this.to.add(toItem);
    return this;
  }

  /**
   * Email TO
   * @return to
  **/
  @ApiModelProperty(required = true, value = "Email TO")
  @NotNull


  public List<String> getTo() {
    return to;
  }

  public void setTo(List<String> to) {
    this.to = to;
  }

  public Mail cc(List<String> cc) {
    this.cc = cc;
    return this;
  }

  public Mail addCcItem(String ccItem) {
    if (this.cc == null) {
      this.cc = new ArrayList<>();
    }
    this.cc.add(ccItem);
    return this;
  }

  /**
   * Email CC
   * @return cc
  **/
  @ApiModelProperty(value = "Email CC")


  public List<String> getCc() {
    return cc;
  }

  public void setCc(List<String> cc) {
    this.cc = cc;
  }

  public Mail bcc(List<String> bcc) {
    this.bcc = bcc;
    return this;
  }

  public Mail addBccItem(String bccItem) {
    if (this.bcc == null) {
      this.bcc = new ArrayList<>();
    }
    this.bcc.add(bccItem);
    return this;
  }

  /**
   * Email BCC
   * @return bcc
  **/
  @ApiModelProperty(value = "Email BCC")


  public List<String> getBcc() {
    return bcc;
  }

  public void setBcc(List<String> bcc) {
    this.bcc = bcc;
  }

  public Mail body(String body) {
    this.body = body;
    return this;
  }

  /**
   * Email Body
   * @return body
  **/
  @ApiModelProperty(value = "Email Body")


  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Mail attachments(List<Attachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public Mail addAttachmentsItem(Attachment attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Upload multiple files
   * @return attachments
  **/
  @ApiModelProperty(value = "Upload multiple files")


  public List<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Mail mail = (Mail) o;
    return Objects.equals(this.subject, mail.subject) &&
        Objects.equals(this.from, mail.from) &&
        Objects.equals(this.to, mail.to) &&
        Objects.equals(this.cc, mail.cc) &&
        Objects.equals(this.bcc, mail.bcc) &&
        Objects.equals(this.body, mail.body) &&
        Objects.equals(this.attachments, mail.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, from, to, cc, bcc, body, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Mail {\n");
    
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    cc: ").append(toIndentedString(cc)).append("\n");
    sb.append("    bcc: ").append(toIndentedString(bcc)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

