package com.trekiz.admin.review.transfersGroup.singleGroup.dao;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.review.transfersGroup.singleGroup.form.TransferForm;

public interface ApplyTransferGroupDao {

	public List<Map<String,Object>> getReviewNew(TransferForm form);
}
