package org.broadinstitute.sting.gatk.iterators;

import net.sf.samtools.SAMRecord;
import net.sf.samtools.util.CloseableIterator;

import java.util.Iterator;

/**
 *
 * User: aaron
 * Date: Apr 14, 2009
 * Time: 5:27:31 PM
 *
 * The Broad Institute
 * SOFTWARE COPYRIGHT NOTICE AGREEMENT 
 * This software and its documentation are copyright 2009 by the
 * Broad Institute/Massachusetts Institute of Technology. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. Neither
 * the Broad Institute nor MIT can be responsible for its use, misuse, or functionality.
 *
 */


/**
 * @author aaron
 * @version 1.0
 * @date Apr 14, 2009
 * <p/>
 * Class BoundedReadIterator
 * <p/>
 * This class implements a read iterator that is bounded by the number of reads
 * it will produce over the iteration.
 */
public class BoundedReadIterator implements CloseableIterator<SAMRecord>, Iterable<SAMRecord> {

    // the genome loc we're bounding
    final private long readCount;
    private long currentCount = 0;
    // the iterator we want to decorate
    private CloseableIterator<SAMRecord> iterator;

    // are we open
    private boolean isOpen = false;
    /**
     * constructor
     * @param iter
     * @param readCount
     */
    public BoundedReadIterator(CloseableIterator<SAMRecord> iter, long readCount) {
        if (iter != null) {
            isOpen = true;
            this.iterator = iter;
        }
        this.readCount = readCount;
    }


    /**
     * Do we have a next? If the iterator has a read and we're not over the read
     * count, then yes
     * @return
     */
    public boolean hasNext() {
        if (isOpen && iterator.hasNext() && currentCount < readCount) {
            return true;
        } else {
            if (isOpen) {
                close();
            }
            return false;
        }
    }

    /**
     * get the next SAMRecord
     * @return SAMRecord representing the next read
     */
    public SAMRecord next() {
        if (!isOpen) {
            throw new UnsupportedOperationException("You cannot call next on a closed iterator");
        }
        ++currentCount;
        return iterator.next();
    }

    /**
     * this is unsupported on SAMRecord iterators
     */
    public void remove() {
        throw new UnsupportedOperationException("You cannot use an iterator to remove a SAMRecord");
    }

    /**
     * close the iterator
     */
    public void close() {
        isOpen = false;
        iterator.close();
    }

    public Iterator<SAMRecord> iterator() {
        return this;
    }
}
